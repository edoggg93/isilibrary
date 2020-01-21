package com.isi.isilibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class IsiAppWebView extends WebView {

    public IsiAppWebView(Context context) {
        super(context);
    }

    public IsiAppWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IsiAppWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IsiAppWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);

        return true;
    }
}
