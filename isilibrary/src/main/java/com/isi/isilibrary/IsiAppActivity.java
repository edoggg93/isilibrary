package com.isi.isilibrary;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.flexbox.FlexboxLayout;

public class IsiAppActivity extends AppCompatActivity{

    private float x1;
    private float y1;
    private static final int MIN_DISTANCE = 400;

    public boolean closing = true;

    private ViewGroup mainView;

    private View inflate = null;
    private View underMenu = null;

    private int height = 0;

    private String leftPackage;
    private String riightPackage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        super.dispatchTouchEvent(ev);

        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y1 = ev.getY();
                x1 = ev.getX();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                height = displayMetrics.heightPixels;

                Log.e(" ", "dispatchTouchEvent: touched: " + y1 + " height: " + height);
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
                }else if(deltay > MIN_DISTANCE && y1 < 100){

                    getApplicationActive();


                }else if(deltay > 40 && y1 > (height - 40)){

                    if(underMenu == null){
                        getPackageLeftRight(0, 203);
                    }else{
                        mainView.removeView(underMenu);

                        underMenu = null;
                    }



                }

                break;
        }

        return false;
    }

    @Override
    public void onBackPressed() {

        if(inflate != null){

            mainView.removeView(inflate);

            inflate = null;

        }else if(underMenu != null){

            mainView.removeView(underMenu);

            underMenu = null;

        }else{
            super.onBackPressed();
        }
    }

    private void presentUnderMenu(){

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;

        underMenu = inflater.inflate(R.layout.under_menu_layout, mainView, false);

        ImageButton left = underMenu.findViewById(R.id.leftImageButton);
        ImageButton right = underMenu.findViewById(R.id.rightImageButton);


        try {
            Drawable icon = getPackageManager().getApplicationIcon(leftPackage);
            left.setImageDrawable(icon);
            Drawable icon2 = getPackageManager().getApplicationIcon(riightPackage);
            right.setImageDrawable(icon2);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(leftPackage);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(riightPackage);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            }
        });

        mainView = (ViewGroup) ((ViewGroup) IsiAppActivity.this.getWindow().getDecorView().getRootView()).getChildAt(0);



        mainView.addView(underMenu);

        Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_up);
        underMenu.startAnimation(bottomUp);

        float scalefactor = mainView.getHeight() * (underMenu.getHeight() / mainView.getHeight());
        float translatey = - underMenu.getHeight() / 2; // Translate amount
        mainView.animate().scaleY(scalefactor).translationYBy(translatey).setDuration(500).start();

    }

    @SuppressLint("InflateParams")
    private void updateGUI(String[] packages){

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            inflate = inflater.inflate(R.layout.menu_layout, null);
        }else{
            inflate = inflater.inflate(R.layout.menu_layout_portrait, null);
        }



        Button closeMenu = inflate.findViewById(R.id.closeMenuButton);

        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.removeView(inflate);

                inflate = null;

            }
        });

        ImageView thisAppImageView = inflate.findViewById(R.id.thisAppImageView);

        try {
            String pkg = getPackageName();//your package name
            Drawable icon = getPackageManager().getApplicationIcon(pkg);
            thisAppImageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException ignored) {

        }

        TextView appName = inflate.findViewById(R.id.thisAppName);

        appName.setText(getApplicationName(null));

        FlexboxLayout flexboxLayout = inflate.findViewById(R.id.serviceFlex);

        for (final String pack : packages){

            if(pack.equals(getPackageName())){
                continue;
            }

            LayoutInflater packInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            assert packInflater != null;
            View packInflate = packInflater.inflate(R.layout.service_flex_cell, null);

            ImageView imageApp = packInflate.findViewById(R.id.appImage);

            try {
                Drawable appIcon = getPackageManager().getApplicationIcon(pack);

                imageApp.setImageDrawable(appIcon);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            TextView appNameSecondary = packInflate.findViewById(R.id.appName);

            appNameSecondary.setText(getApplicationName(pack));

            packInflate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pack);
                    if (launchIntent != null) {
                        mainView.removeView(inflate);

                        inflate = null;
                        startActivity(launchIntent);//null pointer check in case package name was not found
                    }
                }
            });

            flexboxLayout.addView(packInflate);
        }

        ImageButton logout = inflate.findViewById(R.id.logoutButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mainView.removeView(inflate);

                    Intent intent2 = new Intent("timeoutService");
                    intent2.putExtra("time_out", 1);
                    sendBroadcast(intent2);

                }catch (Exception ignored){

                }
            }
        });


        YoYo.with(Techniques.SlideInDown).duration(700).repeat(0).playOn(inflate);

        mainView = ((ViewGroup) IsiAppActivity.this.getWindow().getDecorView().getRootView());

        mainView.addView(inflate);

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

    private void getApplicationActive(){

        try{
            Intent myIntent = new Intent();
            myIntent.setClassName("com.isi.isiapp", "com.isi.isiapp.PackageActivity");
            myIntent.putExtra("intent", "getApplicationsActive");
            startActivityForResult(myIntent, 202);
        }catch (Exception ignored){

        }

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

    private void getPackageLeftRight(int code, int request){

        try{
            Intent myIntent = new Intent();
            myIntent.setClassName("com.isi.isiapp", "com.isi.isiapp.PackageActivity");
            myIntent.putExtra("package_name", getApplicationContext().getPackageName());
            myIntent.putExtra("code", code);
            startActivityForResult(myIntent, request);
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

        }else if (requestCode == 202){

            assert data != null;
            String[] packageName = data.getStringArrayExtra("applications_active");

            updateGUI(packageName);

        }else if (requestCode == 203){

            assert data != null;
            leftPackage = data.getStringExtra("package_name");

            getPackageLeftRight(1, 204);

        }else if (requestCode == 204){

            assert data != null;
            riightPackage = data.getStringExtra("package_name");

            presentUnderMenu();

        }
    }

    public void doSomethingOnTimeout(){

    }

    private String getApplicationName(String packageName) {

        if(packageName == null){
            packageName = getPackageName();
        }

        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo( packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "");
    }


}

