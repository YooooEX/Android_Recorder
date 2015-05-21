package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.schutzstaffel.recorder.R;

import db.DBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonDao {
	private static SQLiteDatabase db;
	private static DBHelper helper;

	public static final String COLUMN_ID = "_id";

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NUMBER = "number";

	private static boolean isIntl = false;

	public static void init(Context context) {
		if (!isIntl) {
			helper = new DBHelper(context);
			db = helper.getWritableDatabase();
			isIntl = true;
		}
	}

	public static boolean insert(String contactName, String contactNumber) {
		long flag = -2;
		if ((contactName != null || contactName != "")
				&& (contactNumber != null || contactNumber != "")
				&& (checkData(contactNumber))) {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_NAME, contactName);
			cv.put(COLUMN_NUMBER, contactNumber);

			flag = db.insert(DBHelper.TB_NAME, null, cv);
		}
		return flag == -1;
	}

	private static boolean checkData(String contactNumber) {
		Cursor c = db.query(DBHelper.TB_NAME, new String[] { COLUMN_NUMBER },
				COLUMN_NUMBER + "=?", new String[] { contactNumber }, null,
				null, null);
		if (c.getCount() > 0) {
			return false;
		}
		return true;
	}

	public static boolean delete(String id) {
		String[] args = new String[] { id };

		db.delete(DBHelper.TB_NAME, COLUMN_ID + "=?", args);
		return false;
	}

	/**
	 * 清除数据
	 */
	public static void deleteAll() {
		db.delete(DBHelper.TB_NAME, null, null);

	}

	/**
	 * 暂时没需要
	 * 
	 */
	public static boolean update(String id, String number) {
		return false;
	}

	public static HashMap<String, Object> get(String number) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String[] args = new String[1];
		args[0] = number;
		Cursor c = db.query(DBHelper.TB_NAME, null, COLUMN_NUMBER + "=?", args,
				null, null, null);
		if (c.moveToFirst()) {
			map.put(COLUMN_NAME, c.getString(c.getColumnIndex(COLUMN_NAME)));
			map.put(COLUMN_NUMBER, c.getString(c.getColumnIndex(COLUMN_NUMBER)));
		}
		return map;
	}

	public static List<HashMap<String, Object>> getAll() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Cursor c = db.query(DBHelper.TB_NAME, null, null, null, null, null,
				null);
		while (c.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(COLUMN_ID, c.getInt(c.getColumnIndex(COLUMN_ID)));
			map.put(COLUMN_NAME, c.getString(c.getColumnIndex(COLUMN_NAME)));
			map.put(COLUMN_NUMBER, c.getString(c.getColumnIndex(COLUMN_NUMBER)));
			map.put("img", R.drawable.ic_launcher);
			list.add(map);
		}
		return list;

	}

}
