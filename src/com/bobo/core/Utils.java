package com.bobo.core;



import java.util.ArrayList;
import java.util.HashMap;

import com.bobo.core.BabyProvider.DatabaseHelper;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class Utils {

	public static final String FILE_NAME = "baby";

	// shared preferences
	public static int getCurrentId(Context context) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return pref.getInt(BabyProvider.ID, -1);
	}

	public static void setCurrentId(Context context, int id) {
		SharedPreferences pref = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(BabyProvider.ID, id);
		editor.commit();
	}

	public static boolean insertRecord(Context context, Record record) {
		ContentValues values = new ContentValues();
		values.put(Record.PID, record.pid);
		values.put(Record.CATEGORY, record.category);
		values.put(Record.TIME, record.time);
		values.put(Record.DESCRIPTION, record.action);
		values.put(Record.PHOTO_FNAME, record.photo_fname);

		try {
			context.getContentResolver().insert(Record.RECORD_URI, values);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean deleteRecord(Context context, String time) {
		ContentValues values = new ContentValues();

		try {
			context.getContentResolver().delete(Record.RECORD_URI, Record.TIME + " = ?", new String[] {time});
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static ArrayList<Record> getRecordListByCat(Context context, int category) {
		ArrayList<Record> list = new ArrayList<Record>();
		Cursor cursor = context.getContentResolver().query(
				Record.RECORD_URI,
				null,
				 Record.CATEGORY + " = ?",
				new String[] {	Integer.toString(category) }, Record.TIME);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Record rec = new Record();
				rec.category = cursor.getInt(cursor
						.getColumnIndexOrThrow(Record.CATEGORY));
				rec.time = cursor.getLong(cursor
						.getColumnIndexOrThrow(Record.TIME));
				rec.action = cursor.getString(cursor
						.getColumnIndexOrThrow(Record.DESCRIPTION));
				rec.photo_fname = cursor.getString(cursor
						.getColumnIndexOrThrow(Record.PHOTO_FNAME));
				list.add(rec);
			}
		}
		cursor.close();
		cursor = null;
		return list;
	}
	public static ArrayList<HashMap<String,String>> getStats(Context context){
		ArrayList<HashMap<String,String>>  list = new ArrayList<HashMap<String,String>> ();
		BabyProvider bp= new BabyProvider();
		SQLiteOpenHelper mOpenHelper = bp.new DatabaseHelper(context);;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//		Cursor cursor =   db.rawQuery("select   strftime('%Y-%m-%d' , time), count(*) from record group by  strftime('%Y-%m-%d' , time) ",null);
//		Cursor cursor =   db.rawQuery("select   date(time/1000, 'unixepoch') , count(description) from record group by date(time/1000, 'unixepoch')  ",null);
		Cursor cursor =   db.rawQuery("select   date(time, 'unixepoch')  , (description) from record  ",null);
		
		if (cursor != null) {
			while (cursor.moveToNext()) {
				HashMap<String,String> detail = new HashMap<String,String>();
				detail.put("date", cursor.getString(0));
				detail.put("count", cursor.getString(1));
				list.add(detail);
			}
		}
		HashMap<String,String> detail = new HashMap<String,String>();
		detail.put("date", "test1");
		detail.put("count", "test2");
		list.add(detail);
		cursor.close();
		cursor = null;
		return list;
	}

}
