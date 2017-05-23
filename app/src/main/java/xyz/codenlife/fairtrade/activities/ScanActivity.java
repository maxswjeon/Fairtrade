package xyz.codenlife.fairtrade.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import xyz.codenlife.fairtrade.R;
import xyz.codenlife.fairtrade.tools.Barcode;
import xyz.codenlife.fairtrade.view.BarcodeScanner;

public class ScanActivity extends AppCompatActivity implements BarcodeScanner.OnBarcodeScanListener{



	private BarcodeScanner scanner;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		scanner = (BarcodeScanner) findViewById(R.id.qrdecoderview);

		scanner.setOnBarcodeScanListener(this);

		scanner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scanner.forceAutoFocus();
				Log.d("ScanActivity", "Force Auto Focused");
			}
		});
		// Use this function to enable/disable decoding
		scanner.enableDecoding();

		// Use this function to change the autofocus interval (default is 5 secs)
		scanner.setAutoFocusInterval(1000L);

		// Use this function to enable/disable Torch
		scanner.setTorchEnabled(true);

	}

	@Override
	protected void onPause() {
		super.onPause();
		scanner.stopCamera();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		scanner.stopCamera();
	}

	@Override
	protected void onResume() {
		super.onResume();
		scanner.startCamera();
	}

	@Override
	public void onScan(String text) {
		//Korea Uses EAN-13 Barcode. If the text length isn't 13, we return it.
		if(text.length() != 13) return;

		//Validate Barcode.
		if(!text.matches("[\\d]*")) return;

		if(!Barcode.checkBarCode(text)) return;

		Intent intent = new Intent();
		intent.putExtra("CODE", text);
		setResult(Activity.RESULT_OK, intent);
		finish();

	}


}
