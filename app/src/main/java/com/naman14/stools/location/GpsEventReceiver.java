
package com.naman14.stools.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GpsEventReceiver extends BroadcastReceiver {

	public static final String GPS_ENABLED_CHANGE = "android.location.GPS_ENABLED_CHANGE";
	public static final String GPS_FIX_CHANGE = "android.location.GPS_FIX_CHANGE";
	public static final long MILLIS_PER_DAY = 86400000;
	
	@Override
	public void onReceive(Context context, Intent intent) {


	}

}
