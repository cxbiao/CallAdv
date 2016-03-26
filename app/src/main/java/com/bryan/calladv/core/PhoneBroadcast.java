package com.bryan.calladv.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PhoneBroadcast extends BroadcastReceiver {


	private static final String TAG = "PhoneService";

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.e(TAG,intent.getAction());
		Intent pIntent=new Intent(context, PhoneService.class);
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			pIntent.putExtra("bInComing",false);
			pIntent.putExtra("outNumber",phoneNumber);
			Log.e(TAG, "call out:" + phoneNumber);
		}
		context.startService(pIntent);
	}

}
