package customs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import utilities.AppInfo;

import com.schutzstaffel.recorder.R;

import dao.PersonDao;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonDialog extends AlertDialog {
	int layoutRes;// 布局文件
	Context context;
	LayoutInflater inflater;
	View dialogView;
	AlertDialog alertDialog;

	public PersonDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public PersonDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * 自定义主题及布局的构造方法
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public PersonDialog(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

	}

	/**
	 * 暂不重构
	 */
	public void showDialog() {
		final EditText et_name, et_number;
		inflater = LayoutInflater.from(context);
		dialogView = inflater.inflate(layoutRes, null);
		et_name = (EditText) dialogView.findViewById(R.id.et_name);
		et_number = (EditText) dialogView.findViewById(R.id.et_number);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setView(dialogView)
				.setIcon(android.R.drawable.ic_dialog_info)

				.setPositiveButton("添加", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String name = et_name.getText().toString();
						String number = et_number.getText().toString()
								.replace(" ", "");
						PersonDao.insert(name, number);

					}
				}).setNegativeButton("取消", null);
		alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void showChooseDialog(String name, HashMap<String, String> data) {
		Iterator iter = data.entrySet().iterator();
		final String contactName = name;
		final String[] contactData = new String[data.size()];
		final boolean[] result;
		int num = 0;
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			contactData[num] = key + ":" + value;
			num++;
			Log.i(AppInfo.TAG, String.valueOf(key + "&" + value));
		}
		result = new boolean[contactData.length];

		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case AlertDialog.BUTTON_POSITIVE:
					for (int i = 0; i < result.length; i++) {
						if (result[i]) {
							String contactNumber[] = contactData[i].split(":");
							PersonDao.insert(contactName + "---"
									+ contactNumber[0], contactNumber[1]);
						}
					}
					break;
				case AlertDialog.BUTTON_NEUTRAL:
					for (int i = 0; i < result.length; i++) {
						String contactNumber[] = contactData[i].split(":");
						PersonDao.insert(
								contactName + "---" + contactNumber[0],
								contactNumber[1]);
					}
					break;
				}

			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("发现多个号码,请选择操作")
				.setMultiChoiceItems(contactData, null,
						new OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								// TODO Auto-generated method stub
								Log.i(AppInfo.TAG, String.valueOf(which) + ":"
										+ isChecked);
								result[which] = isChecked;
							}
						}).setPositiveButton("确定", listener)
				.setNegativeButton("取消", null)
				.setNeutralButton("添加所有", listener);
		alertDialog = builder.create();
		alertDialog.show();
	}
}
