package com.bobo.core;


import com.bobo.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

/* 
 *  
 * Provide interface to default preference 
 * 
 */
public class Setting {


	public static Setting getInstance(Context context) {
		mContext = context;
		if(mInstance == null ){
			mInstance = new Setting(context);
		} else {
			mInstance.reloadPreferences();
		}
		return  mInstance;
	}
	

	/** Private Members **/
	private static final String PREF_BABY_NAME = "baby_name";
	private static final String PREF_ACTION_LIST = "action_list";
	private static final String PREF_ARCHIVE_THRESHOLD = "archive_threshold";
	private static final String PREF_DEFAULT_ACTION = "default_action";
	private static Context mContext; 

	public static final String TAG = "nevin";
	
	private SharedPreferences mPref;
	private static Setting mInstance;
	public String[] mActionList;
	public String mBabyName;
	public int mArchiveThreshold;
	public String mDefaultAction;
	
	
	private Setting(Context context) {
		mPref = PreferenceManager.getDefaultSharedPreferences(context);
		reloadPreferences();
	}

	private void reloadPreferences() {
		mBabyName= mPref.getString(PREF_BABY_NAME, "Baby");
//		mArchiveThreshold= mPref.getInt(PREF_ARCHIVE_THRESHOLD, 0);
		mDefaultAction =  mPref.getString(PREF_DEFAULT_ACTION,  mContext.getString(R.string.atcion_default));
		mActionList = mPref.getString(PREF_ACTION_LIST, "").split(" ");
		Log.d("nevin","mBabyName:"+mBabyName);		
	}


}
