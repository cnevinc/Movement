package com.popo.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


import android.content.Context;
import android.util.Log;


public class Core implements Serializable{

	public static final String TAG = "nevin";
	private static final long serialVersionUID = 1L;

	private static Core mCore;
	transient private static Context mContext;

	private ArrayList<HashMap<String,String>> mLogList;


	private Core(){

	
	}

	public static synchronized Core getCoreInstance(){
		if (mCore==null){
			mCore = new Core();
		} 
		return mCore;
	}

	public static void setContext(Context context){
		mContext = context;
	}

	/////////////////////////////////////////////////////////////////
	private static String fileName = "core.ser";
	public static void save(){
		FileOutputStream fos = null ;
		ObjectOutputStream os = null ;
		try {
			fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);

			if (fos==null){
				Log.e(TAG," Core save but null file");
				return;
			}
			os = new ObjectOutputStream(fos);
			os.writeObject(mCore);
			Log.d(TAG," Core saved!");
		} catch (FileNotFoundException e) {
			Log.e(TAG," ClassNotFoundException"+e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG," IOException"+e.toString());
			e.printStackTrace();
		}finally{
			try {
				if (fos!=null)
					fos.close();
				if (os!=null)
					os.close();
			} catch (IOException e) {
				Log.e(TAG,"IOException closing os "+e);
			}


		}

	}
	public static void load() {
		ObjectInputStream is = null ;
		try {
			FileInputStream fis = mContext.openFileInput(fileName);
			is = new ObjectInputStream(fis);
			mCore  = (Core) is.readObject();
			Log.d(TAG," Core load!" + mCore.toString());
		} catch (FileNotFoundException e) {
			Log.e(TAG," File Not Found ExceptionS:"+e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG," IOExceptionS :"+e.toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(TAG," ClassNotFoundExceptionS:"+e.toString());
			e.printStackTrace();

		}finally{
			try {
				if (is!=null)
					is.close();
			} catch (IOException e) {
				Log.e(TAG," IOException at close "+e.toString());
			}
		}

	}

	public ArrayList<HashMap<String, String>> getLogList() {
		return mLogList;
	}

	public void setLogList(ArrayList<HashMap<String,String>> mLogList) {
		this.mLogList = mLogList;
	}


}
