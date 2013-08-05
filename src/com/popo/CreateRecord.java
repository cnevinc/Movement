package com.popo;

import java.util.Calendar;

import com.popo.core.Record;
import com.popo.core.Utils;
import com.popo.view.CustomAnalogClock;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class CreateRecord extends Activity {

	final static int DIALOG_TIME = 0;

	int mPid, mCat;
	Calendar mTime;

	TextView mTv;
	CustomAnalogClock mClock;
	EditText mEdit;
	Button mBtn1, mBtn2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);

		mTime = Calendar.getInstance();
		mPid = getIntent().getIntExtra(Record.PID, -1);
		mCat = getIntent().getIntExtra(Record.CATEGORY, -1);

		mTv = (TextView) (TextView) findViewById(R.id.tv1);
		mTv.setText(getIntent().getStringExtra(Record.TITLE));

		mEdit = (EditText) findViewById(R.id.edit1);

		mClock = (CustomAnalogClock) findViewById(R.id.clock1);
		mClock.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_TIME);
			}
		});

		mBtn1 = (Button) findViewById(R.id.btn1);
		mBtn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Record rec = new Record();
				rec.pid = mPid;
				rec.category = mCat;
				rec.action = mEdit.getText().toString();
				if (rec.action.equals("")) rec.action=CreateRecord.this.getResources().getString(R.string.atcion_kick);
				rec.time = mTime.getTimeInMillis();
				Utils.insertRecord(CreateRecord.this, rec);
				finish();
			}
		});

		mBtn2 = (Button) findViewById(R.id.btn2);
		mBtn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_TIME:
			return new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
							mTime.set(Calendar.MINUTE, minute);
							mClock.setTime(hourOfDay, minute, 0);
						}
					}, mTime.get(Calendar.HOUR_OF_DAY),
					mTime.get(Calendar.MINUTE), false);
		default:
			return null;
		}
	}
}
