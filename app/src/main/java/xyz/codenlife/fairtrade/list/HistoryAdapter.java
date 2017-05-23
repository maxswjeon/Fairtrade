package xyz.codenlife.fairtrade.list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.codenlife.fairtrade.R;

/**
 * Created by maxsw on 2017-05-01.
 */

public class HistoryAdapter extends BaseAdapter {

	private Context _context;
	private ArrayList<HistoryData> _data = new ArrayList<>();

	public HistoryAdapter(Context context) {
		super();
		_context = context;
	}

	@Override
	public int getCount() {
		return _data.size();
	}

	@Override
	public Object getItem(int position) {
		return _data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HistoryItem holder;
		if (convertView == null) {
			holder = new HistoryItem();

			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_history_item, null);

			holder.Icon = (ImageView) convertView.findViewById(R.id.list_image);
			holder.Title = (TextView) convertView.findViewById(R.id.list_title);
			holder.Code = (TextView) convertView.findViewById(R.id.list_code);

			convertView.setTag(holder);
		}else{
			holder = (HistoryItem) convertView.getTag();
		}

		HistoryData mData = _data.get(position);

		if (mData.Icon != null) {
			holder.Icon.setVisibility(View.VISIBLE);
			holder.Icon.setImageDrawable(mData.Icon);
		}else{
			holder.Icon.setVisibility(View.VISIBLE);
			holder.Icon.setImageDrawable(null);
		}

		holder.Title.setText(mData.Title);
		holder.Code.setText(mData.Code);

		return convertView;
	}

	public void addItem(Drawable icon, String mTitle, String mDate){
		HistoryData addInfo = new HistoryData();
		addInfo.Icon = icon;
		addInfo.Title = mTitle;
		addInfo.Code = mDate;

		_data.add(addInfo);
	}

	public void remove(int position){
		_data.remove(position);
		dataChange();
	}

	public void dataChange(){
		notifyDataSetChanged();
	}
}
