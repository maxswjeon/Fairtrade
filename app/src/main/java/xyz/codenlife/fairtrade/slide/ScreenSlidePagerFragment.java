package xyz.codenlife.fairtrade.slide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import xyz.codenlife.fairtrade.R;

/**
 * Created by maxsw on 2017-05-07.
 */

public class ScreenSlidePagerFragment extends Fragment {

	private int position;

	public static ScreenSlidePagerFragment newInstance(int page) {
		ScreenSlidePagerFragment fragmentFirst = new ScreenSlidePagerFragment();
		Bundle args = new Bundle();
		args.putInt("POSITION", page);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt("POSITION");
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_slide_page, container,false);

		TextView titleText = (TextView)view.findViewById(R.id.info_title);
		TextView contentText = (TextView)view.findViewById(R.id.info_content);
		ImageView imageView = (ImageView)view.findViewById(R.id.info_image);
		Button button = (Button)view.findViewById(R.id.info_button);

		titleText.setText(ScreenSlidePagerData.values()[position].Title);
		contentText.setText(ScreenSlidePagerData.values()[position].Content);
		imageView.setRotation(ScreenSlidePagerData.values()[position].Rotation);
		if(ScreenSlidePagerData.values()[position].Image != 0)
			imageView.setImageResource(ScreenSlidePagerData.values()[position].Image);

		if(position == ScreenSlidePagerData.values().length - 1){
			button.setText("OK");
		}

		return view;
	}


}
