package xyz.codenlife.fairtrade.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.MultiFormatOneDReader;

import java.io.IOException;
import java.lang.ref.WeakReference;

import static android.hardware.Camera.getCameraInfo;


public class BarcodeScanner extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

	public interface OnBarcodeScanListener{
		void onScan(String text);
	}

	private OnBarcodeScanListener _callback;
	private MultiFormatOneDReader _reader;
	private CameraManager _cameraManager;
	private int _previewWidth;
	private int _previewHeight;
	private boolean _decodingEnabled = true;


	private DecodeTask _decodeTask;

	public BarcodeScanner(Context context) {
		super(context, null);
	}

	public BarcodeScanner(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode()) return;
		if(getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
			_cameraManager = new CameraManager(getContext());
			_cameraManager.setPreviewCallback(this);
			getHolder().addCallback(this);
			setPreviewCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
			getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		else{
			throw new RuntimeException("Error : Camera not found.");
		}
	}

	public void setOnBarcodeScanListener(OnBarcodeScanListener listener) {
		_callback = listener;
	}

	public void stopCamera(){
		_cameraManager.stopPreview();
	}

	public void startCamera(){
		//If CameraManager is null, Assume we are restating
		if(_cameraManager == null) {
			_cameraManager = new CameraManager(getContext());
			_cameraManager.setPreviewCallback(this);
			getHolder().addCallback(this);
			setPreviewCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
			getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		_cameraManager.startPreview();
	}

	public void setAutoFocusInterval(long millis){
		if(_cameraManager!=null)
			_cameraManager.setAutofocusInterval(millis);
	}

	public void forceAutoFocus(){
		if(_cameraManager!=null)
			_cameraManager.forceAutoFocus();
	}

	public void setTorchEnabled(boolean enabled) {
		if (_cameraManager != null) {
			_cameraManager.setTorchEnabled(enabled);
		}
	}

	public void enableDecoding(){
		_decodingEnabled = true;
	}

	public void disableDecoding(){
		_decodingEnabled = false;
	}

	public void setPreviewCameraId(int cameraId) {
		_cameraManager.setPreviewCameraId(cameraId);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		try{
			_cameraManager.openDriver(holder, getWidth(), getHeight());
		}
		catch (IOException e){
			_cameraManager.closeDriver();
		}

		try{
			_reader = new MultiFormatOneDReader(null);
			_cameraManager.startPreview();
		}
		catch (Exception e){
			_cameraManager.closeDriver();
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (holder.getSurface() == null) return;
		if (_cameraManager.getPreviewSize() == null) return;

		_previewWidth = _cameraManager.getPreviewSize().x;
		_previewHeight = _cameraManager.getPreviewSize().y;

		_cameraManager.stopPreview();

		_cameraManager.setPreviewCallback(this);
		_cameraManager.setDisplayOrientation(getCameraDisplayOrientation());
		_cameraManager.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		_cameraManager.setPreviewCallback(null);
		_cameraManager.stopPreview();
		_cameraManager.closeDriver();
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if (!_decodingEnabled || _decodeTask != null
				&& _decodeTask.getStatus() == AsyncTask.Status.RUNNING) {
			return;
		}

		_decodeTask = new DecodeTask(this);
		_decodeTask.execute(data);
	}

	@SuppressWarnings("deprecation")
	private int getCameraDisplayOrientation() {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.GINGERBREAD) {
			return 90;
		}

		Camera.CameraInfo info = new Camera.CameraInfo();
		getCameraInfo(_cameraManager.getPreviewCameraId(), info);
		WindowManager windowManager =
				(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		int rotation = windowManager.getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
			default:
				break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		return result;
	}

	public static class DecodeTask extends AsyncTask<byte[], Void, Result>{

		private final WeakReference<BarcodeScanner> viewRef;

		public DecodeTask(BarcodeScanner scanner){
			viewRef = new WeakReference<BarcodeScanner>(scanner);
		}

		@Override
		protected Result doInBackground(byte[]... params) {
			final BarcodeScanner view = viewRef.get();
			if(view == null) return null;

			final PlanarYUVLuminanceSource source =
					view._cameraManager.buildLuminanceSource(params[0],
															view._previewWidth,
															view._previewHeight);

			final HybridBinarizer hybin = new HybridBinarizer(source);
			final BinaryBitmap bitmap = new BinaryBitmap(hybin);

			try{
				return view._reader.decode(bitmap);
			}
			catch (NotFoundException e){
				Log.d("BarcodeScanner", "NOT FOUND");
			}
			catch (FormatException e){
				Log.d("BarcodeScanner", "FORMAT ERROR");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Result result) {
			super.onPostExecute(result);

			final BarcodeScanner view = viewRef.get();
			if(result!=null)
				view._callback.onScan(result.getText());
		}
	}

}
