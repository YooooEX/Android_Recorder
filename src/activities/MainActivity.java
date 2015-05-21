package activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import services.RecordService;
import utilities.AppInfo;
import utilities.RecordAction;

import com.schutzstaffel.recorder.R;

import customs.PersonDialog;
import dao.PersonDao;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {

	private Button btn_RecordStart, btn_RecordStop, btn_target, btn_manual_add,
			btn_list;
	Intent serviceIntent;
	String[] contact = new String[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		serviceIntent = new Intent(MainActivity.this, RecordService.class);

		PersonDao.init(MainActivity.this);

		btn_RecordStart = (Button) findViewById(R.id.btn_start);
		btn_RecordStop = (Button) findViewById(R.id.btn_stop);
		btn_target = (Button) findViewById(R.id.btn_target);
		btn_manual_add = (Button) findViewById(R.id.btn_manual_add);
		btn_list = (Button) findViewById(R.id.btn_list);

		btn_RecordStop.setEnabled(false);

		btn_RecordStart.setOnClickListener(click);
		btn_RecordStop.setOnClickListener(click);
		btn_target.setOnClickListener(click);
		btn_manual_add.setOnClickListener(click);
		btn_list.setOnClickListener(click);

	}

	private View.OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_start:
				startService(serviceIntent);
				btn_RecordStart.setEnabled(false);
				btn_RecordStop.setEnabled(true);
				break;
			case R.id.btn_stop:
				stopService(serviceIntent);
				btn_RecordStart.setEnabled(true);
				btn_RecordStop.setEnabled(false);
				break;
			case R.id.btn_target:
				Intent pickAction = new Intent();
				pickAction.setAction(Intent.ACTION_PICK);
				pickAction.setData(ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(pickAction, 1);
				break;
			case R.id.btn_manual_add:

				PersonDialog dialog = new PersonDialog(MainActivity.this,
						R.layout.dialog_person);
				dialog.showDialog();
				break;
			case R.id.btn_list:
				startActivity(new Intent(MainActivity.this,
						TargetActivity.class));
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == MainActivity.RESULT_OK && requestCode == 1) {
			Uri contactData = data.getData();
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			getContact(cursor);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getContact(Cursor cursor) {
		int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		int phoneNum = cursor.getInt(phoneColumn);
		if (phoneNum > 0) {
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			// 获得联系人电话的cursor
			Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null,
					Phone.CONTACT_ID + "=" + contactId, null, null);
			if (phones.moveToFirst()) {
				HashMap<String, String> nums = new HashMap<String, String>();
				String name = null;
				String number = null;
				int phone_type;
				int otherType = 0;
				for (; !phones.isAfterLast(); phones.moveToNext()) {
					name = phones.getString(phones
							.getColumnIndex(Phone.DISPLAY_NAME));
					number = phones.getString(phones
							.getColumnIndex(Phone.NUMBER));
					phone_type = phones.getInt(phones
							.getColumnIndex(Phone.TYPE));
					switch (phone_type) {
					case Phone.TYPE_HOME:
						nums.put("家庭", number);
						break;
					case Phone.TYPE_WORK:
						nums.put("工作", number);
						break;
					case Phone.TYPE_MOBILE:
						nums.put("移动电话", number);
						break;
					default:
						nums.put("其他类型" + (++otherType), number);
					}

				}
				if (nums.size() > 1) {
					PersonDialog dialog = new PersonDialog(MainActivity.this,
							R.layout.list_activity_mainactivity_choose_dialog);
					dialog.showChooseDialog(name,nums);
				}
				if (nums.size() == 1) {
					PersonDao.insert(name, number);
				}
				if (nums.size() < 1) {
					Toast.makeText(MainActivity.this, "此联系人无电话号码!",
							Toast.LENGTH_LONG);
				}
			}
			if (!phones.isClosed()) {
				phones.close();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int action = item.getItemId();
		switch (action) {
		case R.id.action_mode_mic:
			RecordAction.RECORD_MODE = MediaRecorder.AudioSource.MIC;
			Toast.makeText(MainActivity.this, "模式已设置为:Mic", 1000).show();
			break;
		case R.id.action_mode_voice_call:
			new AlertDialog.Builder(MainActivity.this)
					.setTitle("警告")
					.setMessage("此模式不保证录音效果,是否继续?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									RecordAction.RECORD_MODE = MediaRecorder.AudioSource.VOICE_CALL;
									Toast.makeText(MainActivity.this,
											"模式已设置为:VOICE_CALL", 1000).show();
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.action_settings:
			return true;
		case R.id.action_exit:
			stopService(serviceIntent);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		if (RecordAction.IS_RECORDING) {
			// 如果正在录音，停止并释放资源
			try {
				RecordAction.MR.stop();
				RecordAction.MR.release();
				RecordAction.MR = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		super.onDestroy();
	}
}