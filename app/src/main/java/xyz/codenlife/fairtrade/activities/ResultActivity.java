package xyz.codenlife.fairtrade.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import xyz.codenlife.fairtrade.R;
import xyz.codenlife.fairtrade.tools.BarcodeInfo;
import xyz.codenlife.fairtrade.tools.FairBarcodes;

public class ResultActivity extends AppCompatActivity{

	private TextView label_name;
	private TextView label_code;
	private ImageView result_image;
	private AdView adView;

	private static final int SCANRESULT = 0;
	private	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		label_name = (TextView)findViewById(R.id.label_product_name);
		label_code = (TextView)findViewById(R.id.label_product_code);
		result_image = (ImageView)findViewById(R.id.result_image);
		adView = (AdView)findViewById(R.id.adView);

		adView.loadAd(new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build());

		preferences = getSharedPreferences("history", MODE_PRIVATE);

		if(getIntent().getBooleanExtra("SCAN", false))
			startActivityForResult(new Intent(this, ScanActivity.class), SCANRESULT);
		else{
			label_code.setText(getIntent().getStringExtra("CODE"));

			if(FairBarcodes.productlist.contains(getIntent().getStringExtra("CODE")))
				result_image.setImageResource(R.drawable.ic_fairtrade_logo1);

			final BarcodeInfo infothread = new BarcodeInfo(getIntent().getStringExtra("CODE"));
			infothread.setOnBarcodeInfoUpdatedListener(new BarcodeInfo.OnBarcodeInfoUpdatedListener() {
				@Override
				public void onUpdated(Boolean succeeded) {
					Handler handler = new Handler(ResultActivity.this.getMainLooper());

					final boolean success = succeeded;

					handler.post(new Runnable() {
						@Override
						public void run() {
							if(success) {
								label_name.setText(infothread.getProductName());
								String s = FairBarcodes.productlist.contains(infothread.getCode())?"true":"false";
								preferences.edit().putString(infothread.getProductName(), infothread.getCode() + s).apply();
							}
							else
								label_name.setText("Failed to get Product Name");
						}
					});

				}
			});
			infothread.start();

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case SCANRESULT: {
				if(resultCode == Activity.RESULT_OK){

					final String code = data.getStringExtra("CODE");

					if(FairBarcodes.productlist.contains(code))
						result_image.setImageResource(R.drawable.ic_fairtrade_logo1);

					label_code.setText(code);

					final BarcodeInfo infothread = new BarcodeInfo(code);
					infothread.setOnBarcodeInfoUpdatedListener(new BarcodeInfo.OnBarcodeInfoUpdatedListener() {
						@Override
						public void onUpdated(Boolean succeeded) {
							Handler handler = new Handler(ResultActivity.this.getMainLooper());

							final boolean success = succeeded;

							handler.post(new Runnable() {
								@Override
								public void run() {
									if(success) {
										label_name.setText(infothread.getProductName());
										String s = FairBarcodes.productlist.contains(infothread.getCode()) ? "true" : "false";
										preferences.edit().putString(infothread.getProductName(), infothread.getCode() + s).apply();
									}
									else
										label_name.setText("Failed to get Product Name");
								}
							});

						}
					});
					infothread.start();

				}
				else if(resultCode == Activity.RESULT_CANCELED){
					finish();
				}
			}
		}
	}

}
