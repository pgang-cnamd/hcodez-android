package com.hcodez.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.hcodez.android.R;

public class PopFind extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popfindwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .2));

        //WindowManager.LayoutParams params = getWindow().getAttributes();
    }
}