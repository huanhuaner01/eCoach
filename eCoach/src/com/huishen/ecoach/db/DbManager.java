package com.huishen.ecoach.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.huishen.ecoach.ui.msg.AppMessage;
import com.huishen.ecoach.ui.msg.AppMessage.MessageType;
import com.huishen.ecoach.ui.msg.IMsgStorage;

/**
 * @author Muyangmin
 * @create 2015-2-20
 */
public final class DbManager implements IMsgStorage {
	
	private static final String LOG_TAG = "DbManager";

	private DbHelper helper;
	private SQLiteDatabase db;
	
	protected static final String TABLE_MESSAGE = "t_appmsg";		//消息表
	protected static final String COLUMN_ID = "_id";				//主键
	protected static final String COLUMN_MSG_TIME = "time";			//时间列
	protected static final String COLUMN_MSG_CONTENT = "content";	//内容列
	protected static final String COLUMN_MSG_TITLE = "title";		//标题列
	protected static final String COLUMN_MSG_DETAIL = "detailurl";	//详情列
	protected static final String COLUMN_MSG_TYPE = "msgtype";		//类型列
	
	public DbManager(Context context) {
		helper = new DbHelper(context);
		try {
			db = helper.getWritableDatabase();
		} catch (SQLiteException e) {
			Log.e(LOG_TAG, "Cannot get sqlite database."+e.getMessage());
			e.printStackTrace();
		}
	}
	

	@Override
	public ArrayList<AppMessage> getAppMessages() {
		String[] projection = new String[] { COLUMN_ID, COLUMN_MSG_CONTENT,
				COLUMN_MSG_DETAIL, COLUMN_MSG_TIME, COLUMN_MSG_TITLE,
				COLUMN_MSG_TYPE };
		Cursor cursor = db.query(TABLE_MESSAGE, projection, null, null, null,
				null, COLUMN_ID);
		ArrayList<AppMessage> list = new ArrayList<AppMessage>();
		while (cursor.moveToNext()) {
			list.add(parseValues(cursor));
		}
		return list;
	}

	/**
	 * @return 插入成功则返回true，并且参数中的对象的Id字段会被设置为在数据库中的ID值;否则返回false。
	 * @see IMsgStorage#saveAppMessage(AppMessage)
	 */
	@Override
	public boolean saveAppMessage(AppMessage msg) {
		if (msg==null){
			return false;
		}
		long id = db.insert(TABLE_MESSAGE, null, getContentValues(msg));
		if (id != -1){
			msg.setId(id);
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public void clearAllMessages() {
		db.delete(TABLE_MESSAGE, null, null);
	}
	
	private ContentValues getContentValues(AppMessage msg){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_MSG_CONTENT, msg.getContent());
		cv.put(COLUMN_MSG_DETAIL, msg.getDetailURL());
		cv.put(COLUMN_MSG_TIME, msg.getTime());
		cv.put(COLUMN_MSG_TITLE, msg.getTitle());
		cv.put(COLUMN_MSG_TYPE, msg.getType().ordinal());//存储的是从0开始的序号, int类型
		return cv;
	}
	
	private AppMessage parseValues(Cursor cursor){
		AppMessage msg = new AppMessage();
		msg.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
		msg.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_MSG_CONTENT)));
		msg.setDetailURL(cursor.getString(cursor.getColumnIndex(COLUMN_MSG_DETAIL)));
		msg.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_MSG_TITLE)));
		msg.setTime(cursor.getLong(cursor.getColumnIndex(COLUMN_MSG_TIME)));
		msg.setType(MessageType.values()[cursor.getInt(cursor.getColumnIndex(COLUMN_MSG_TYPE))]);//还原枚举
		return msg;
	}
}
