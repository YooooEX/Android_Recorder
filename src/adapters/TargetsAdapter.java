package adapters;

import com.schutzstaffel.recorder.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TargetsAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;

	public TargetsAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_targets_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.name = (TextView) convertView.findViewById(R.id.target_name);
			holder.number = (TextView) convertView
					.findViewById(R.id.target_number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	static class ViewHolder {
		public ImageView img;
		public TextView name;
		public TextView number;
	}

}
