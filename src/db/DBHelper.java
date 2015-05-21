package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "contacts";
	public static final String TB_NAME = "person";
	private static final int version = 1;
	private SQLiteDatabase database;

	public DBHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DB_NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String query = "create table if not exists " + TB_NAME
				+ " (_id integer primary key autoincrement,"
				+ "name varchar(64)," + "number varchar(64),"
				+ "UNIQUE(`number`))";
		db.execSQL(query);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
