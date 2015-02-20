package com.huishen.ecoach.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.huishen.ecoach.db.DbManager.*;

/**
 * @author Muyangmin
 * @create 2015-2-20
 */
public final class DbHelper extends SQLiteOpenHelper {

	private static final String LOG_TAG = "DbHelper";
	
	protected static final String DATABASE_NAME = "ecoach.db";
	private static final int DATABASE_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DbHelper(Context context, String dbname) {
		super(context, dbname, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlmsg = buildMessageTable();
		Log.d(LOG_TAG, sqlmsg);
		try {
			db.beginTransaction();
			db.execSQL(sqlmsg);	
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (SQLiteException e) {
			Log.e(LOG_TAG, "Cannot create tables.");
			e.printStackTrace();
		}
	}
	
	private final String buildMessageTable(){
		StringBuilder sb = new StringBuilder();
		sb.append("create table IF NOT EXISTS ").append(TABLE_MESSAGE)
		.append("(")
		.append(COLUMN_ID).append(" integer primary key autoincrement, ")
		.append(COLUMN_MSG_TIME).append(" varchar, ")
		.append(COLUMN_MSG_TITLE).append(" varchar, ")
		.append(COLUMN_MSG_TYPE).append(" integer, ")
		.append(COLUMN_MSG_CONTENT).append(" varchar, ")
		.append(COLUMN_MSG_DETAIL).append(" varchar")
		.append(")");
		return sb.toString();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// current version:empty
	}

}
