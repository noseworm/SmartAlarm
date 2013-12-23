package com.example.smartalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyReceiver extends BroadcastReceiver {
	@Override
    public void onReceive(Context context, Intent intent)
    {
	   Bundle bundle = intent.getExtras();
       Intent service1 = new Intent(context, SetPhysicalAlarm.class);
       service1.putExtras(bundle);
       context.startService(service1);
    }   
}
