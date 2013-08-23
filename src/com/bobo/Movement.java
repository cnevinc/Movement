package com.bobo;


import java.util.ArrayList;
import java.util.HashMap;

import com.bobo.core.Core;
import com.bobo.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Movement extends Activity {

	private String TAG = "nevin";

	ListView mListView;
	ArrayList<HashMap<String,String>> mLogList = new ArrayList<HashMap<String,String>>();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_movement, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movement);

		// initial Data ---start---
		Core.setContext(Movement.this);
		Core.load(); 
		Core system = Core.getCoreInstance();
		this.mLogList = system.getLogList();
		if (mLogList==null){
			mLogList = new ArrayList<HashMap<String,String>>();
		}
		Log.d(TAG,"mLogList ["+mLogList.size()+"] mListView["+mListView+"]");

		// initial Data ----end----

		// initial ListView ---start---
		mListView = (ListView)findViewById(R.id.listView1);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder adb=new AlertDialog.Builder(Movement.this);
				adb.setTitle("Delete?");
				adb.setMessage("½T©w§R°£ ?" );
				final int positionToRemove = position;
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mLogList.remove(positionToRemove);
						mListView.invalidateViews();

					}});
				adb.show();				return false;
			}
		});
		mListView.setAdapter(
				(ListAdapter) new SimpleAdapter( 
						this, 
						mLogList ,
						android.R.layout.simple_list_item_1,
						new String[] { "date" },
						new int[] { android.R.id.text1 } 
						)
				);
		// initial ListView ----end----
	}

	@Override
	public void onPause() {
		super.onPause();
		Core.getCoreInstance().setLogList(mLogList);
		Core.getCoreInstance().save();
	}



}
