package com.whb.myhandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class HandlerActivity extends Activity {
	private static final String SHAREDPREFERENCES = "SharedInfo";
	private static final String sharedpreferences = "sharedinfo";
	private static final String KEY = "String-Value";
	private static final String PACKAGE_NAME = "com.whb.preferencetestactivity";
	
	private HandlerThread mSubHandlerThread;
	
	private MyHandler mHandler;
	private MySubHandler mSubHandler;
	
	private Context mContext;
	private TextView mText;
	private static final String TAG = "HandlerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setWindowFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | 
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		
		setContentView(R.layout.activity_main);
		
		mContext = this;
		mText = (TextView)findViewById(R.id.display);
		
		Log.d(TAG, "onCreate(), this= " + this);
		
		for(int i=0; i<1; i++) {
			mSubHandlerThread = new HandlerThread("mSubHandlerThread"+ i){
				@Override
				public void run() {
					Log.d(this.getName(), "run()....");
					super.run();
				}
			};
			mSubHandlerThread.start();
			Log.d(TAG, "" + mSubHandlerThread.getName() + " is running? ");
		}
		
		try{
		    mSubHandlerThread.wait();
		} catch (Exception ex) {
			// donothing;
		}
		mSubHandler = new MySubHandler(this, mSubHandlerThread.getLooper());
		mSubHandler.obtainMessage(99).sendToTarget();
		mHandler = new MyHandler();
		
		/*
		mHandler.sendEmptyMessage(0);
		mSubHandler.sendEmptyMessage(0);
		*/
		Message mSubMsg = mSubHandler.obtainMessage(0);
		
		Bundle bundle = new Bundle();
		bundle.putBoolean("FLAG", true);
		bundle.putString("NAME", "Naruto");
		
		mSubMsg.setData(bundle);
		mSubMsg.sendToTarget();
		
		Message mMsg = mHandler.obtainMessage(0);
		mMsg.sendToTarget();
	}

	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onNewIntent(), this= " + this);
		super.onNewIntent(intent);
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStart(), this= " + this);
		super.onStart();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onRestart(), this= " + this);
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume(), this= " + this);
		super.onResume();
		parseUri();
		
		testClone();
		
		//testReadOtherApplicationSharedPreference(PACKAGE_NAME, SHAREDPREFERENCES, KEY);
		testReadOtherApplicationSharedPreference(PACKAGE_NAME, sharedpreferences, KEY);
		
		testSynchronized();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause(), this= " + this);
		super.onPause();
		//testActivity(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStop(), this= " + this);
		
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy(), this= " + this);
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	@Override
	public void recreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "recreate(), this= " + this);
		super.recreate();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	class MyHandler extends Handler {
		private final String TAG = "MyHandler";
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.d(TAG, "handleMessage()... my thread name: " + Thread.currentThread().getName());
			/*
			Toast.makeText(mContext, TAG + " | Thread name: \n" + 
							Thread.currentThread().getName(), Toast.LENGTH_LONG).show();
							*/
		}
		
		public MyHandler() {}
		
		public MyHandler(Looper looper) {
			super(looper);
		}
	};
	
	static class MySubHandler extends Handler{
		private final String TAG = "MySubHandler";
		private final WeakReference<HandlerActivity> mWFActivity;
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle bundle = msg.getData();
			boolean flag = bundle.getBoolean("FLAG");
			String name = bundle.getString("NAME");
			
			Log.d(TAG, "handleMessage()... my thread name: " + Thread.currentThread().getName());
			switch(msg.what) {
				case 99:
					Log.d("MySubHandler", "received message 99;");
					break;
					
				default:
					break;
			}
			/*
			if(mWFActivity.get() != null) {
				Toast.makeText(mWFActivity.get(), TAG + "\nFLAG: " + flag + 
						" | NAME: " + name, Toast.LENGTH_LONG).show();
			}
			*/
		}
		
		public MySubHandler(HandlerActivity activity) {
			mWFActivity = new WeakReference<HandlerActivity>(activity);
		}
		
		public MySubHandler(HandlerActivity activity, Looper looper) {
			super(looper);
			mWFActivity = new WeakReference<HandlerActivity>(activity);
		}
	}
	
	private void parseUri() {
		final String AUTHORITY = "com.android.contacts";
	    final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
	    final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(AUTHORITY_URI,
                "phone_lookup");
	    final Uri uri1 = Uri.withAppendedPath(CONTENT_FILTER_URI, "0268683314");
	    final Uri uri2 = Uri.withAppendedPath(CONTENT_FILTER_URI, "+421268683314");
	    final Uri uri3 = Uri.withAppendedPath(CONTENT_FILTER_URI, "+421911450300");
	    
	    Log.d(TAG, "uri1 = " + uri1);
	    Log.d(TAG, "uri2 = " + uri2);
	    Log.d(TAG, "uri3 = " + uri3);
	}
	
	private void testClone() {
		class Member {
			String mAddress;
			int mIndex;
			
			public Member(String address, int index) {
				mAddress = address;
				mIndex = index;
			}
			
			public String dump() {
				Log.d(TAG, "address = " + mAddress + ", index = " + mIndex);
				return "address: " + mAddress + ", index: " + mIndex + "\n";
			}
		}
		
		ArrayList<Member> mMembers = new ArrayList<Member>();
		String displayInfo = "";
		
		Member member1 = new Member("10000", 1);
		mMembers.add(member1);
		
		Member member2 = new Member("10010", 1);
		mMembers.add(member2);
		
		Member member3 = new Member("10086", 1);
		mMembers.add(member3);
		
		displayInfo += "before change clone\n";
		for(int i=0, s=mMembers.size(); i<s; i++) {
			displayInfo += mMembers.get(i).dump();
		}
		
		List<Member> copyMembers = (List<Member>) mMembers.clone();
		for(int i=0, s=copyMembers.size(); i<s; i++) {
			Member member = copyMembers.get(i);
			member.mAddress = "10000" + i;
			member.mIndex = 10 + i;
		}
		
		displayInfo += "-------------------------------------------------------------\n";
		displayInfo += "after change clone\n";
		for(int i=0, s=mMembers.size(); i<s; i++) {
			displayInfo += mMembers.get(i).dump();
		}
		
		mText.setText(displayInfo);
	}
	
	private void testActivity(Context context) {
		Intent intent = new Intent("com.whb.myhandler.SHOW_ROAMING_REMINDER");
		intent.setClass(context, CallReceiver.class);
		context.sendBroadcast(intent);
	}
	
	@SuppressWarnings("deprecation")
	private void testReadOtherApplicationSharedPreference(String packageName, String sharedPreferencesName, String keyName) {
		if(packageName == null || sharedPreferencesName== null) {
			return;
		}
		
		try {
			Context mOtherApplicationContext = mContext.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);
			SharedPreferences mSP = mOtherApplicationContext.getSharedPreferences(sharedPreferencesName, MODE_WORLD_READABLE);
			
			String value = mSP.getString(keyName, "can't readable");
			Toast.makeText(mContext, "get app(" + packageName +  ")'s prefs(" 
						+ sharedPreferencesName + ") key(" + keyName + ")" + value, 
						Toast.LENGTH_LONG).show();
		} catch (NameNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	private void testSynchronized() {
		class TestClass {
			Object mLock = new Object();
			
			public void print(String thread) {
				synchronized(mLock){
					for(int i=0; i<5; i++) {
						Log.d("TestClass", thread + ":" + i);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						// mLock.wait 的调用, 将会释放 mLock 同步锁, 让给其他线程执行任务.
						mLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		final TestClass mTestClass = new TestClass();
		
		new Thread(new Runnable() {
			public void run() {
				mTestClass.print(Thread.currentThread().getName());
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				mTestClass.print(Thread.currentThread().getName());
			}
		}).start();
	}
	
	private void testLogPrinter(Context context) {
		getMainLooper().setMessageLogging(new LogPrinter(Log.DEBUG, "Main-Looper"));
	}
	
	private void setWindowFlags(int flags) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.flags |= flags;
		getWindow().setAttributes(lp);
	}

}
