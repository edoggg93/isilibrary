package com.isi.isilibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class IsiAppScrollView extends ScrollView {

    public IsiAppScrollView(Context context) {
        super(context);
    }

    public IsiAppScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IsiAppScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IsiAppScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);

        return true;
    }
}
