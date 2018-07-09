package com.whb.myhandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "HandlerActivity";
    private static final int MSG_START_HANDLEACTIVITY = 0x01;
    
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Log.d(TAG, "[CallReceiver]: onReceive intent: " + arg1.getAction());
		if(arg1.getAction().equals("com.whb.myhandler.SHOW_ROAMING_REMINDER")) {
			showRoamingReminderDialog(arg0);
		} else if(arg1.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			Message msg = mHandler.obtainMessage(MSG_START_HANDLEACTIVITY, arg0);
			// mHandler.sendMessageDelayed(msg, 10000);
			testTurnScreenOnFlag(arg0);
		}
	}
	
	private void showRoamingReminderDialog(Context context) {
		Log.d(TAG, "[CallReceiver]: showRoamingReminderDialog()");
		
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, ErrorDialog.class);
		context.startActivity(intent);
	}
	
	private void startHandlerActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, HandlerActivity.class);
		context.startActivity(intent);
	}

	public static class ErrorDialog extends Activity {
		AlertDialog mAlertDialog = null;
		static final int DELAY_SHOW_HANDLER_ACTIVITY = 1;
		Context mContext = null;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			Log.d(TAG, "[ErrorDialog]: onCreate()");
			super.onCreate(savedInstanceState);
			
			mContext = this;
			mAlertDialog = new AlertDialog.Builder(this).setTitle("Roaming alert")
					.setMessage("do you want to real dialing under roaming state")
					.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					})
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							finish();
						}
					}).create();
		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			Log.d(TAG, "[ErrorDialog]: onResume()");
			super.onResume();
			
			if(mAlertDialog != null) {
				mAlertDialog.setCancelable(false);
				mAlertDialog.show();
			}
			///Message msg = myHandler.obtainMessage(DELAY_SHOW_HANDLER_ACTIVITY);
			//myHandler.sendMessageDelayed(msg, 15000);
		}

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			Log.d(TAG, "[ErrorDialog]: onDestroy()");
			super.onDestroy();
		}

		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			Log.d(TAG, "[ErrorDialog]: onPause(), mAlertDialog = " + mAlertDialog);
			if(mAlertDialog != null) {
				mAlertDialog.dismiss();
				mAlertDialog = null;
			}
			super.onPause();
		}

		@Override
		protected void onStart() {
			// TODO Auto-generated method stub
			Log.d(TAG, "[ErrorDialog]: onStart()");
			super.onStart();
		}

		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			Log.d(TAG, "[ErrorDialog]: onStop()");
			super.onStop();
			
			finish();
		}
		
		Handler myHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch(msg.what) {
				case DELAY_SHOW_HANDLER_ACTIVITY:
					Log.d(TAG, "[ErrorDialog]: receive DELAY_SHOW_HANDLER_ACTIVITY");
					mAlertDialog.show();
					break;
					
				default:
					// do nothing.
					break;
				}
			}
		};
    }
	
	/// test FLAG_TURN_SCREEN_ON start
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.d(TAG, "[CallReceiver]: handleMessage(), msg = " + msg.what);
			switch(msg.what) {
			case MSG_START_HANDLEACTIVITY:
				testTurnScreenOnFlag((Context)msg.obj);
				break;
				
			default:
				break;
			}
		}
	};
	
	private static void testTurnScreenOnFlag(Context context) {
		Log.d(TAG, "[CallReceiver]: testTurnScreenOnFlag()...");
		Intent intent = new Intent();
		intent.setClass(context, HandlerActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
}