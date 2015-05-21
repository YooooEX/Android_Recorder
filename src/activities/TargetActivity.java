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

	// ViewHolder��̬��
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
			// �ڴ�������������������ݼ��е���Ŀ��
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// ��ȡ���ݼ�����ָ��������Ӧ��������
			return position;
		}

		@Override
		public long getItemId(int position) {
			// ��ȡ���б�����ָ��������Ӧ����id
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			// ��ȡһ�������ݼ���ָ����������ͼ����ʾ����
			// �������convertViewΪ�գ�����Ҫ����View
			if (convertView == null) {
				holder = new ViewHolder();
				// �����Զ����Item���ּ��ز���
				convertView = mInflater.inflate(R.layout.list_targets_item,
						null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.name = (TextView) convertView
						.findViewById(R.id.target_name);
				holder.number = (TextView) convertView
						.findViewById(R.id.target_number);
				// �����úõĲ��ֱ��浽�����У�������������Tag��Ա���淽��ȡ��Tag
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
					.setIcon(android.R.drawable.ic_dialog_alert).setTitle("����")
					.setMessage("���Ҫ���б������?")
					.setPositiveButton("ȷ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							PersonDao.deleteAll();
							data.clear();
							adapter.notifyDataSetChanged();
						}
					}).setNegativeButton("ȡ��", null).show();
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
			new AlertDialog.Builder(TargetActivity.this).setTitle("����")
					.setMessage("ȷ��Ҫɾ�����������?")
					.setPositiveButton("ȷ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							PersonDao.delete(target);
							data.remove(pos);
							adapter.notifyDataSetChanged();
						}
					}).setNegativeButton("ȡ��", null).show();
			return false;
		}
	};
}