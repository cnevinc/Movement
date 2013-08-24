package com.bobo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bobo.core.Record;
import com.bobo.core.Utils;
import com.bobo.view.CustomAnalogClock;
import com.bobo.R;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
 
public class CreateRecord extends Activity {

	final static int DIALOG_TIME = 0;

	int mPid, mCat;
	Calendar mTime;

	TextView mTv;
	CustomAnalogClock mClock;
	EditText mEdit;
	Button mBtn1, mBtn2, mBtn3;
	TextView mTv1 ;
	
	// Take Pictures
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private Uri mFilePathUri;

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
				rec.photo_fname = mFilePathUri.toString();
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
		
		// Take pictures, mod by Nevin 20130823
		mBtn3 = (Button) findViewById(R.id.btn3);
		mBtn3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// create Intent to take a picture and return control to the calling application
			    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			    mFilePathUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, mFilePathUri); // set the image file name

			    // start the image capture Intent
			    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			    Toast.makeText(CreateRecord.this, "Image saved to:\n" +
			    		mFilePathUri.toString() , Toast.LENGTH_LONG).show();
			}
		});		
		
		mTv1 = (TextView)findViewById(R.id.tv_msg);
		Spinner sp = (Spinner) this.findViewById(R.id.spin_action);
		ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item ,new String[]{"¿ËÁý ¥k   ¥ª","²~Áý","ºÎÄ±","§¿§¿","¤j«K","Ä_Ä_¦b½ð"});		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener (){

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int pos, long id) {
				CreateRecord.this.mEdit.setText(adapterView.getSelectedItem().toString()) ;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
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
	
	
	// Take pictures, mod by Nevin 20130823, Saving Media Files
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	        	Log.d("nevin"," File saved at " + mFilePathUri + " and returned result");
	    		mTv1.setText(CreateRecord.this.getResources().getString(R.string.msg_file_saved) + this.mFilePathUri.toString());
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }

	    if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Video captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Video saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }
	}
}
