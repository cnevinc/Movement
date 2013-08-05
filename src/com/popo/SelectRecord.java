package com.popo;

import java.util.ArrayList;
import java.util.HashMap;

import com.popo.core.BabyProvider;
import com.popo.core.Record;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class SelectRecord extends Activity {

	int mPid;
	GridView mGrid;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);

		mPid = getIntent().getIntExtra(BabyProvider.ID, -1);
		mGrid = (GridView) findViewById(R.id.grid1);

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String[] str_arrays = getResources().getStringArray(
				R.array.record_array);
		int[] img_arrays = { R.drawable.feeder, R.drawable.event };
		for (int i = 0; i < str_arrays.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("tv1", str_arrays[i]);
			map.put("img1", img_arrays[i]);
			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.grid_item, new String[] { "tv1", "img1" }, new int[] {
						R.id.tv1, R.id.img1 });
		mGrid.setAdapter(adapter);
		mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(SelectRecord.this, RecordList.class);
				intent.putExtra(Record.PID, mPid);
				intent.putExtra(Record.CATEGORY, position);
				intent.putExtra(Record.TITLE,
						(String) ((HashMap<String, Object>) mGrid.getAdapter()
								.getItem(position)).get("tv1"));
				startActivity(intent);
			}
		});
	}
}