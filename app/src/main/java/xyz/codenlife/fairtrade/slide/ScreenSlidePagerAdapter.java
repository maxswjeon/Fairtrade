package xyz.codenlife.fairtrade.slide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by maxsw on 2017-05-07.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

	public ScreenSlidePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		ScreenSlidePagerFragment fragment = new ScreenSlidePagerFragment();
		Bundle b = new Bundle();
		b.putInt("POSITION", position);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	public int getCount() {
		return ScreenSlidePagerData.values().length;
	}

}
