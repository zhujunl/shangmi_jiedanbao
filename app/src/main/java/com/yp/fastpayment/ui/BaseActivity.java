package com.yp.fastpayment.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yp.fastpayment.view.CustomToast;


public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    public boolean isPause;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        initSysBar();
        setContentView(layoutId());
        initView();
        initData();
    }

    void initSysBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    protected abstract int layoutId();

    protected abstract void initData();

    protected abstract void initView();


    public String getResStr(int resId) {
        return getResources().getString(resId);
    }

    public Drawable getResDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public void showToast(String msg) {
        CustomToast.showShort(mContext, msg);
    }

    public void openActivity(Class cls) {
        startActivity(new Intent(this, cls));
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    private static String[] PERMISSIONS = {Manifest.permission.CAMERA};
    final int REQUEST_PERMISSION_CODE = 10001;

    boolean checkCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
