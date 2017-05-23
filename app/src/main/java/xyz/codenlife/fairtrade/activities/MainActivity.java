package xyz.codenlife.fairtrade.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;

import java.io.IOException;

import xyz.codenlife.fairtrade.R;
import xyz.codenlife.fairtrade.tools.Barcode;

public class MainActivity extends AppCompatActivity {

	private Button button_scan;
	private Button button_image;
	private Button button_history;
	private Button button_text;

	private ImageView infoImage;
	private ImageView aboutImage;

	private static final int CAMERA_PERMISSION = 113;
	private static final int FILESYSTEM_PERMISSION = 153;
	private static final int GET_IMAGE = 123;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button_scan = (Button)findViewById(R.id.button_scan);
		button_image = (Button)findViewById(R.id.button_image);
		button_history=(Button)findViewById(R.id.button_history);
		button_text = (Button)findViewById(R.id.button_text);

		infoImage = (ImageView)findViewById(R.id.image_info);
		aboutImage = (ImageView)findViewById(R.id.image_about);

		button_scan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (ContextCompat.checkSelfPermission(MainActivity.this,
						Manifest.permission.CAMERA)
						!= PackageManager.PERMISSION_GRANTED) {

					// Should we show an explanation?
					if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
							Manifest.permission.CAMERA)) {

						new AlertDialog.Builder(MainActivity.this)
								.setMessage("Need Camera Permission to Scan!!")
								.setNeutralButton("OK",null)
								.show();

					} else {

						// No explanation needed, we can request the permission.

						ActivityCompat.requestPermissions(MainActivity.this,
								new String[]{Manifest.permission.CAMERA},
								CAMERA_PERMISSION);

						// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
						// app-defined int constant. The callback method gets the
						// result of the request.
					}
				}
				else{
					startActivity(new Intent(MainActivity.this, ResultActivity.class)
							.putExtra("SCAN", true)
							.putExtra("CODE", (String)null));
				}
			}
		});

		button_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ContextCompat.checkSelfPermission(MainActivity.this,
						Manifest.permission.READ_EXTERNAL_STORAGE)
						!= PackageManager.PERMISSION_GRANTED) {

					// Should we show an explanation?
					if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
							Manifest.permission.CAMERA)) {

						new AlertDialog.Builder(MainActivity.this)
								.setMessage("Need Filesystem Permission to Scan Image!!")
								.setNeutralButton("OK",null)
								.show();

					} else {

						// No explanation needed, we can request the permission.

						ActivityCompat.requestPermissions(MainActivity.this,
								new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
								FILESYSTEM_PERMISSION);

						// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
						// app-defined int constant. The callback method gets the
						// result of the request.
					}
				}
				else{
					Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
					gallery.setType("image/*");
					startActivityForResult(gallery, GET_IMAGE);
				}
			}

		});

		button_history.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, HistoryActivity.class));
			}
		});

		button_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, TextActivity.class));
			}
		});

		infoImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(MainActivity.this)
						.setMessage("FairTrade by\n" +
									"Park \n" +
									"Do \n" +
									"Sung Hyun Tae\n" +
									"Jeon Sang Wan\n" +
									"Copyright 2017(c) All rights Reserved\n" +
									"\n" +
									"All Advertisement Profit goes to \n" +
									"Korea Fair Trade Association, KFTA")
						.setNeutralButton("Close", null)
						.show();
			}
		});

		aboutImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, InfoActivity.class));
			}
		});


		startActivity(new Intent(this, SplashActivity.class));

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case CAMERA_PERMISSION: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					startActivity(new Intent(MainActivity.this, ResultActivity.class)
							.putExtra("SCAN", true)
							.putExtra("CODE", (String)null));

				} else {

					new AlertDialog.Builder(this)
							.setMessage("Need Camera Permission to Scan!!")
							.setNeutralButton("OK",null)
							.show();
				}

				break;
			}
			case FILESYSTEM_PERMISSION: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
					gallery.setType("image/*");
					startActivityForResult(gallery, GET_IMAGE);

				} else {

					new AlertDialog.Builder(this)
							.setMessage("Need Filesystem Permission to Scan Image!!")
							.setNeutralButton("OK",null)
							.show();
				}

				break;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode!=RESULT_OK)
			return;

		switch(requestCode){
			case GET_IMAGE: {
				Uri imageUri = data.getData();
				try {
					String result = Barcode.ScanFromFile(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri));

					startActivity(new Intent(MainActivity.this, ResultActivity.class)
							.putExtra("SCAN", false)
							.putExtra("CODE", result));

				}
				catch (IOException e){
					new AlertDialog.Builder(this)
							.setMessage("Cannot Open File!")
							.setNeutralButton("OK", null)
							.show();
				}
				catch (NotFoundException e){
					new AlertDialog.Builder(this)
							.setMessage("Barcode Not found!")
							.setNeutralButton("OK", null)
							.show();
				}
				catch (FormatException e) {
					new AlertDialog.Builder(this)
							.setMessage("Unsupported Barcode Format!")
							.setNeutralButton("OK", null)
							.show();
				}
				catch (Barcode.EANFormatException e){
					new AlertDialog.Builder(this)
							.setMessage("Barcode Validation Failed!")
							.setNeutralButton("OK", null)
							.show();
				}
			}
		}
	}
}
