package com.isi.isilibrary;

import android.app.Activity;
import android.content.Intent;

public class NotifyBroadcast {

    public static void sendBroadcast(Activity a, String title, String message){
        Intent i = new Intent("notify_isiapp_interface");
        i.putExtra("title", title);
        i.putExtra("message", message);
        a.sendBroadcast(i);
    }

}
