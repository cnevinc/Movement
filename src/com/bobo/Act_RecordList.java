package com.bobo;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.bobo.core.Record;
import com.bobo.core.Setting;
import com.bobo.core.Utils;
import com.bobo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class Act_RecordList extends Activity {

	int mPid, mCat;
	
	ListView mListViewStats;
	ListView mListView;
	
	ArrayList<HashMap<String,String>> mStatList = new ArrayList<HashMap<String,String>>();
	ArrayList<Record> mRecordList;

	RecordAdapter mAdapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_movement, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.menu_dlog:
			intent.setClass(Act_RecordList.this, Act_CreateRecord.class);
			intent.putExtra(Record.PID, mPid);
			intent.putExtra(Record.CATEGORY, mCat);
			startActivity(intent);
			return true;
		case R.id.menu_qlog:
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Date date = new Date();
			String strDate = sdFormat.format(date);
			Record rec = new Record();
			rec.pid = mPid;
			rec.category = mCat;
			rec.action = Setting.getInstance(this).mBabyName + Setting.getInstance(this).mDefaultAction;
			rec.time = date.getTime();
			Utils.insertRecord(this, rec); 
			mAdapter.updateList(Utils.getRecordListByCat(this,  mCat));
			mAdapter.notifyDataSetChanged();
			return true;  
		case R.id.menu_setting:
			intent.setClass(Act_RecordList.this, Act_Setting.class);
			startActivity(intent);
			return true;
		/*
		 * case R.id.menu_stats:
			mListViewStats.setAdapter(
					(ListAdapter) new SimpleAdapter( 
							this, 
							 Utils.getStats(this) ,
							android.R.layout.simple_list_item_2,
							new String[] { "date" ,"count"},
							new int[] { android.R.id.text1,android.R.id.text2 } 
							)
					);
			 
			if (mListViewStats.getVisibility()==View.VISIBLE){
				mListViewStats.setVisibility(View.GONE);
				this.mListView.setVisibility(View.VISIBLE);
				item.setTitle(R.string.menu_stats);
			}else{
				mListViewStats.setVisibility(View.VISIBLE);
				this.mListView.setVisibility(View.GONE);
				item.setTitle(R.string.menu_list);
			}
			return true;
		*/
		default:
			return super.onOptionsItemSelected(item);
		}
	}
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_list);

		mPid = getIntent().getIntExtra(Record.PID, -1);
		mCat = getIntent().getIntExtra(Record.CATEGORY, -1);

		// Statistics
//		mListViewStats = (ListView) findViewById(R.id.lv_stats);
//		mListViewStats.setAdapter(
//				(ListAdapter) new SimpleAdapter( 
//						this, 
//						 Utils.getStats(this) ,
//						android.R.layout.simple_list_item_2,
//						new String[] { "date" ,"count"},
//						new int[] { android.R.id.text1,android.R.id.text2 } 
//						)
//				);
		
		// Record List
		mListView = (ListView) findViewById(R.id.list1);
		mAdapter = new RecordAdapter(this, null);		// Initial collection of records when OnResume
		mListView.setAdapter(mAdapter);

		// listener
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
					long id) {
				String fileUri = mRecordList.get(position).photo_fname;
				if (fileUri!=null){
					AlertDialog.Builder adb=new AlertDialog.Builder(Act_RecordList.this);
					adb.setTitle("Open File");
					adb.setMessage(R.string.msg_open_photo);
					adb.setNegativeButton(R.string.msg_cancel, null);
					adb.setPositiveButton(R.string.msg_confirm, new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String fileUri = mRecordList.get(position).photo_fname;
							Uri imageUri = Uri.parse(fileUri);
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(imageUri, "image/*");
							startActivity(intent);
						}});
					adb.show();		
				}
				
			}
			
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int positionToRemove = position;
				AlertDialog.Builder adb=new AlertDialog.Builder(Act_RecordList.this);
				adb.setTitle(R.string.msg_delete);
				adb.setMessage(R.string.msg_confirm_del );
				adb.setNegativeButton(R.string.msg_cancel, null);
				adb.setPositiveButton(R.string.msg_confirm, new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Utils.deleteRecord(Act_RecordList.this, Long.toString(mRecordList.get(positionToRemove).time));
						mAdapter.updateList(Utils.getRecordListByCat(Act_RecordList.this,  mCat));
						mAdapter.notifyDataSetChanged();
					}});
				adb.show();			
				return false;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.updateList(Utils.getRecordListByCat(this,  mCat));
		mAdapter.notifyDataSetChanged();
	}
	
	class RecordAdapter extends BaseAdapter {

		Context mContext;
		LayoutInflater mInflater;
		SimpleDateFormat mFormat;

		public RecordAdapter(Context context, ArrayList<Record> list) {
			mContext = context;
			mRecordList = list;
			mInflater = LayoutInflater.from(context);
			mFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		}

		public void updateList(ArrayList<Record> list) {
			mRecordList = list;
		}

		public int getCount() {
			if (mRecordList == null) {
				return 0;
			}
			return mRecordList.size();
		}

		public Object getItem(int position) {
			if (mRecordList == null) {
				return null;
			} 
			return mRecordList.get(position);
		} 

		public long getItemId(int position) {
			return position;
		}  

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.record_item, null);
			}

			TextView tv1 = (TextView) convertView.findViewById(R.id.tv1);
			TextView tv2 = (TextView) convertView.findViewById(R.id.tv2);
			ImageView iv1 = (ImageView) convertView.findViewById(R.id.imageView1);
			
			Record rec = mRecordList.get(position);
			tv1.setText(rec.action);
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(rec.time);
			tv2.setText(mFormat.format(c.getTime()));
			
			// Only initial humbnail when photo_fname is not empty
			if (rec.photo_fname!=null && !rec.photo_fname.equals("")){				
				Uri imgUri=Uri.parse(rec.photo_fname);
				iv1.setImageBitmap(Utils.decodeSampleBitmapFromUri(imgUri, 50 , 50));	// decode bitmaps with 50x50 size thumbnail
			}else{
				// If no photo, clear the image
				iv1.setImageBitmap(null);
			}
			return convertView;
		}
		
	}

}
