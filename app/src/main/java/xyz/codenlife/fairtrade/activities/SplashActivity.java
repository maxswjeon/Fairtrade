package xyz.codenlife.fairtrade.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;

import xyz.codenlife.fairtrade.R;
import xyz.codenlife.fairtrade.tools.FairBarcodes;

public class SplashActivity extends AppCompatActivity {

	private final static int MESSAGE_DONE = 0;
	private final static int MESSAGE_OPEN_INFO=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Handler handler = new Handler(){
			@Override
			public void handleMessage(Message message){
				if(message.what == MESSAGE_DONE){
					finish();
				}
				else{
					startActivity(new Intent(SplashActivity.this, InfoActivity.class));
					finish();
				}
			}
		};
		MobileAds.initialize(getApplicationContext(), "ca-app-pub-4777473171625834~2573527306");
		FairBarcodes.Init();
		SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
		if(!preferences.contains("Info")){
			handler.sendEmptyMessageDelayed(MESSAGE_OPEN_INFO, 3000);
			preferences.edit().putBoolean("Info", true).apply();
		}else {
			handler.sendEmptyMessageDelayed(MESSAGE_DONE, 3000);
		}
	}
}
