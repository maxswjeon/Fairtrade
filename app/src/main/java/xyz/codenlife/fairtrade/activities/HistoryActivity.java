package xyz.codenlife.fairtrade.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Map;

import xyz.codenlife.fairtrade.R;
import xyz.codenlife.fairtrade.list.HistoryAdapter;
import xyz.codenlife.fairtrade.list.HistoryData;

public class HistoryActivity extends AppCompatActivity {

	private AdView adView;
	private ListView listView;
	private HistoryAdapter adapter;
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		adView = (AdView)findViewById(R.id.adView2);

		adView.loadAd(new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build());

		preferences = getSharedPreferences("history", MODE_PRIVATE);

		listView = (ListView)findViewById(R.id.list_history);
		adapter = new HistoryAdapter(this);

		listView.setAdapter(adapter);

		Drawable d;
		for (Map.Entry<String, ?> s: preferences.getAll().entrySet()) {
			d = null;
			if(s.getValue().toString().substring(13).equals("true"))
				d = getResources().getDrawable(R.drawable.ic_fairtrade_logo1);
			adapter.addItem(d, s.getKey(), s.getValue().toString().substring(0, 13));
		}

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HistoryData d = (HistoryData)adapter.getItem(position);
				startActivity(new Intent(HistoryActivity.this, ResultActivity.class)
								.putExtra("SCAN", false)
								.putExtra("CODE", d.Code));
			}
		});

	}
}
