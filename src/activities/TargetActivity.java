package activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilities.AppInfo;

import com.schutzstaffel.recorder.R;

import dao.PersonDao;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class TargetActivity extends Activity {

	private ListView lv;
	private List<HashMap<String, Object>> data;
	MyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.list_activity_targets);
		super.onCreate(savedInstanceState);
		lv = (ListView) findViewById(R.id.lv);
		data = new ArrayList<HashMap<String, Object>>();
		data = getData();
		adapter = new MyAdapter(this);
		lv.setAdapter(adapter);
		lv.setOnItemLongClickListener(tlcl);
	}

	private List<HashMap<String, Object>> getData() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		list = PersonDao.getAll();
		return list;
	}

	// ViewHolder静态类
	static class ViewHolder {
		public ImageView img;
		public TextView name;
		public TextView number;
	}

	class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater = null;

		private MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// 在此适配器中所代表的数据集中的条目数
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// 获取数据集中与指定索引对应的数据项
			return position;
		}

		@Override
		public long getItemId(int position) {
			// 获取在列表中与指定索引对应的行id
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			// 获取一个在数据集中指定索引的视图来显示数据
			// 如果缓存convertView为空，则需要创建View
			if (convertView == null) {
				holder = new ViewHolder();
				// 根据自定义的Item布局加载布局
				convertView = mInflater.inflate(R.layout.list_targets_item,
						null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.name = (TextView) convertView
						.findViewById(R.id.target_name);
				holder.number = (TextView) convertView
						.findViewById(R.id.target_number);
				// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setBackgroundResource((Integer) data.get(position).get(
					"img"));
			holder.name.setText((String) data.get(position).get("name"));
			holder.number.setText((String) data.get(position).get("number"));

			return convertView;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.target, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int action = item.getItemId();
		if (action == R.id.action_delete_all) {
			new AlertDialog.Builder(TargetActivity.this)
					.setIcon(android.R.drawable.ic_dialog_alert).setTitle("警告")
					.setMessage("真的要把列表清空吗?")
					.setPositiveButton("确认", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							PersonDao.deleteAll();
							data.clear();
							adapter.notifyDataSetChanged();
						}
					}).setNegativeButton("取消", null).show();
		}
		return super.onOptionsItemSelected(item);
	}

	OnItemLongClickListener tlcl = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			Log.i(AppInfo.TAG, String.valueOf(data.get(position).get("number"))
					+ "-----" + String.valueOf(data.get(position).get("name"))
					+ "-----" + String.valueOf(data.get(position).get("_id")));

			final String target = String.valueOf(data.get(position).get("_id"));
			final int pos = position;
			new AlertDialog.Builder(TargetActivity.this).setTitle("警告")
					.setMessage("确定要删除这个号码吗?")
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							PersonDao.delete(target);
							data.remove(pos);
							adapter.notifyDataSetChanged();
						}
					}).setNegativeButton("取消", null).show();
			return false;
		}
	};
}