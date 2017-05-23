package xyz.codenlife.fairtrade.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xyz.codenlife.fairtrade.R;
import xyz.codenlife.fairtrade.slide.ScreenSlidePagerAdapter;
import xyz.codenlife.fairtrade.slide.ScreenSlidePagerData;

public class InfoActivity extends FragmentActivity {

	private ViewPager _viewPager;
	private PagerAdapter _pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		_viewPager = (ViewPager) findViewById(R.id.info_pager);
		_pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		_viewPager.setAdapter(_pagerAdapter);
	}

	@Override
	public void onBackPressed() {
		if(_viewPager.getCurrentItem() == 0){
			super.onBackPressed();
		}
		else{
			_viewPager.setCurrentItem(_viewPager.getCurrentItem() - 1);
		}
	}

	public void toNext(View view) {
		if(_viewPager.getCurrentItem() == ScreenSlidePagerData.values().length - 1)
			finish();
		_viewPager.setCurrentItem(_viewPager.getCurrentItem() + 1);
	}
}
