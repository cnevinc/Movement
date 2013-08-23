package com.bobo.core;

import android.net.Uri;

public class Record {

	public final static String PID = "pid";
	public final static String CATEGORY = "category";
	public final static String TIME = "time";
	public final static String DESCRIPTION = "description";
	public final static String PHOTO_FNAME = "photo_fname";	
	public final static Uri RECORD_URI = Uri
			.parse("content://com.bobo/record");

	public final static String TITLE = "title";
 
	public int id;
	public int pid;
	public int category;
	public long time;
	public String action;
	public String photo_fname;
}
