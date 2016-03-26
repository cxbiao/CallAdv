package com.bryan.calladv.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class PhoneService extends Service {


	private static final String TAG = "PhoneService";
	private PhoneTipController phoneTipController;
	private boolean bInComing=true;
	private String outNumber="";
	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG, "onBind");
		return null;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG, "onCreate");
		TelephonyManager telephonyManager=
				(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");
		if(intent!=null){
			bInComing=intent.getBooleanExtra("bInComing",true);
			String ret=intent.getStringExtra("outNumber");
			outNumber=TextUtils.isEmpty(ret)?outNumber:ret;
			Log.e(TAG, "receiveNumber:"+outNumber);
		}
		return START_STICKY;
	}
	
	class MyPhoneListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			
		    switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: //空间
				Log.e(TAG, "电话空闲" + incomingNumber);
				if(phoneTipController!=null){
					phoneTipController.hide();
					phoneTipController=null;
				}

				break;
			case TelephonyManager.CALL_STATE_RINGING: //来电状态
				Log.e(TAG, "来电话了"+incomingNumber);
				bInComing=true;
				if(phoneTipController==null){
					phoneTipController=new PhoneTipController(getApplicationContext(),incomingNumber);
					phoneTipController.show();
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
				incomingNumber= TextUtils.isEmpty(incomingNumber)?outNumber:incomingNumber;
				Log.e(TAG, "正在通信" + incomingNumber);
				if(phoneTipController==null){
					phoneTipController=new PhoneTipController(getApplicationContext(),incomingNumber);
					phoneTipController.show();
				}

				break;
			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

		
	}
	

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");

	}
}
