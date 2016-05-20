package com.example.micha.bigstylenotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by micha on 5/20/2016.
 */
public class MyReceiver extends BroadcastReceiver {

    public String ComponentName() {
        return this.getClass().getName();
    }

    @Override
    public void onReceive(Context context, Intent intent) {


    }
}
