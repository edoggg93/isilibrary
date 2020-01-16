package com.isi.isilibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IsiAppActivity extends AppCompatActivity{

    private float x1;
    static final int MIN_DISTANCE = 400;

    public boolean closing = true;

    private boolean swapped = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(swapped){
     /*Make sure you don't swap twice,
since the dispatchTouchEvent might dispatch your touch event to this function again!*/
            swapped = false;
            return super.onTouchEvent(event);
        }

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE && x2 > x1)
                {

                    getPackageNameSlide(0);

                }else if(Math.abs(deltaX) > MIN_DISTANCE && x2 < x1){
                    getPackageNameSlide(1);
                }

                swapped = true;

                break;
        }
        return super.onTouchEvent(event);
    }

    private final BroadcastReceiver guestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            final int message = intent.getIntExtra("time_out", 0);

            if(message != 0){

                final Context copy = context;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        doSomethingOnTimeout();

                        if(closing){
                            Log.e("", "run: i'm closing" );
                            Toast.makeText(copy, "i'm closing", Toast.LENGTH_SHORT).show();
                            finish();
                            System.exit(0);
                        }else{
                            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.isi.isiapp");
                            if (launchIntent != null) {
                                startActivity(launchIntent);//null pointer check in case package name was not found
                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                            }
                        }


                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver(guestReceiver, new IntentFilter("timeoutService"));


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);

    }

    private void getPackageNameSlide(int code){

        Intent myIntent = new Intent();
        myIntent.setClassName("com.isi.isiapp", "com.isi.isiapp.PackageActivity");
        myIntent.putExtra("package_name", getApplicationContext().getPackageName());
        myIntent.putExtra("code", code);
        if(code == 0){
            startActivityForResult(myIntent, 200);
        }else{
            startActivityForResult(myIntent, 201);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200){

            if(resultCode == RESULT_OK){

                assert data != null;
                String packageName = data.getStringExtra("package_name");

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }


            }

        }else if(requestCode == 201){

            if(resultCode == RESULT_OK){

                assert data != null;
                String packageName = data.getStringExtra("package_name");

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }


            }

        }
    }

    public void doSomethingOnTimeout(){

    }


}

