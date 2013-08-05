package com.popo.core;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class BabyProvider extends ContentProvider {

	final static String DB_NAME = "baby.db";
	final static int DB_VERSION = 5;
	final static String RECORD_TB = "record";
	final static String AUTHORITY = "com.popo";
	final static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

	final static int RECORD = 2;
	final static int RECORD_ID = 3;

	public final static String ID = "_id";

	static {
		matcher.addURI(AUTHORITY, "record", RECORD);
		matcher.addURI(AUTHORITY, "record/#", RECORD_ID);
	}

	SQLiteOpenHelper mOpenHelper = null;

	final class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(final Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(final SQLiteDatabase db) {
			createTable(db);
		}

		@Override
		public void onUpgrade(final SQLiteDatabase db, final int oldV,
				final int newV) {
			dropTable(db);
			createTable(db);
		}
	}

	private void createTable(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE " + RECORD_TB + "(" + ID
					+ " INTEGER PRIMARY KEY, " + Record.PID + " INTEGER, "
					+ Record.CATEGORY + " INTEGER, " + Record.TIME
					+ " INTEGER, " + Record.DESCRIPTION + " TEXT);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void dropTable(SQLiteDatabase db) {
		try {
			db.execSQL("DROP TABLE IF EXISTS " + RECORD_TB);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String tableName = "";
		String condition = "";
		int match = matcher.match(uri);
		switch (match) {
		case RECORD:
			tableName = RECORD_TB;
			break;
		case RECORD_ID:
			tableName = RECORD_TB;
			condition = ID + "=" + uri.getPathSegments().get(1);
			selection = (TextUtils.isEmpty(selection) ? condition : condition
					+ " AND ( " + selection + ")");
			break;
		}
		int count = db.delete(tableName, selection, selectionArgs);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String tableName = "";
		Uri newUri = null;
		int match = matcher.match(uri);
		switch (match) {
		case RECORD:
			tableName = RECORD_TB;
			newUri = Record.RECORD_URI;
			break;
		}
		long row = db.insert(tableName, null, values);
		return ContentUris.withAppendedId(newUri, row);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		int match = matcher.match(uri);
		switch (match) {
		case RECORD:
			qb.setTables(RECORD_TB);
			break;
		case RECORD_ID:
			qb.setTables(RECORD_TB);
			qb.appendWhere(ID + "=" + uri.getPathSegments().get(1));
			break;
		}
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String tableName = "";
		String condition = "";
		int match = matcher.match(uri);
		switch (match) {
		case RECORD:
			tableName = RECORD_TB;
			break;
		case RECORD_ID:
			tableName = RECORD_TB;
			condition = ID + "=" + uri.getPathSegments().get(1);
			selection = (TextUtils.isEmpty(selection) ? condition : condition
					+ " AND ( " + selection + ")");
			break;
		}
		int count = db.update(tableName, values, selection, selectionArgs);
		return count;
	}

}
