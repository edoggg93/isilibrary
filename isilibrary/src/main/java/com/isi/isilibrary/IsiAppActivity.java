package com.isi.isilibrary;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class IsiAppActivity extends AppCompatActivity{

    private float x1;
    private float y1;
    static final int MIN_DISTANCE = 400;

    public boolean closing = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y1 = ev.getY();
                x1 = ev.getX();
                break;

            case MotionEvent.ACTION_UP:

                float y2 = ev.getY();
                float deltay = Math.abs(y2-y1);
                float x2 = ev.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE && x2 > x1)
                {

                    getPackageNameSlide(0);

                }else if(Math.abs(deltaX) > MIN_DISTANCE && x2 < x1){
                    getPackageNameSlide(1);
                }else if(deltay > MIN_DISTANCE && y1 < 40){
                    LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    assert inflater != null;
                    @SuppressLint("InflateParams") final View inflate = inflater.inflate(R.layout.menu_layout, null);

                    final ViewGroup mainView = ((ViewGroup) IsiAppActivity.this.getWindow().getDecorView().getRootView());

                    Button closeMenu = inflate.findViewById(R.id.closeMenuButton);

                    closeMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mainView.removeView(inflate);
                        }
                    });

                    YoYo.with(Techniques.SlideInDown).duration(1000).repeat(0).playOn(inflate);

                    mainView.addView(inflate);

                }

                break;
        }
        super.onTouchEvent(ev);
        return true;
    }

    private final BroadcastReceiver guestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int message = intent.getIntExtra("time_out", 0);

            if(message != 0){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        doSomethingOnTimeout();

                        if(closing){
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

    private void getPackageNameSlide(int code){

        try{
            Intent myIntent = new Intent();
            myIntent.setClassName("com.isi.isiapp", "com.isi.isiapp.PackageActivity");
            myIntent.putExtra("package_name", getApplicationContext().getPackageName());
            myIntent.putExtra("code", code);
            if(code == 0){
                startActivityForResult(myIntent, 200);
            }else{
                startActivityForResult(myIntent, 201);
            }
        }catch (Exception ignored){

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
