package com.yp.fastpayment.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.yp.fastpayment.R;
import com.yp.fastpayment.api.MyRetrofit;
import com.yp.fastpayment.util.SharedPreferenceUtil;

/**
 * Created by Administrator on 2020/3/20.
 */

public class IpSetActivity extends BaseActivity implements View.OnClickListener {
    EditText et_ip_address;
    TextView tv_set;
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ip_set);
        init();
    }*/

    @Override
    protected int layoutId() {
        return R.layout.act_ip_set;
    }

    @Override
    protected void initData() {
//        initTitle();
        bindDefault();
        registerClick();
    }

    @Override
    protected void initView() {
        et_ip_address = findViewById(R.id.et_ip_address);
        tv_set = findViewById(R.id.tv_set);
    }

    private void bindDefault() {
        et_ip_address.setText(MyRetrofit.ipAddress);
        et_ip_address.setSelection(et_ip_address.getText().toString().length());
    }


    private void registerClick() {
        tv_set.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_set:
                clickSetIP();
                break;
        }
    }

    private void clickSetIP() {
        String ipAddress = et_ip_address.getText().toString();
        if(TextUtils.isEmpty(ipAddress)){
            showToast("请输入ip地址");
            return;
        }
        if(!ipAddress.contains("http")){
            showToast("请输入正确ip地址");
            return;
        }
        SharedPreferenceUtil.getInstance(this).putString("IpAddress",ipAddress);

        MyRetrofit.ipAddress = ipAddress;

        showToast("IP地址设置成功");
        finish();
    }
    /*private void reStartApp()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis()+500, restartIntent); // 0.5秒钟后重启应用
//                CacheActivityUtils.finishActivity();
                System.exit(0);
            }
        },1000);

    }*/

//    private void initTitle() {
//        mTitle.setMiddleTextTop("设置IP地址");
//    }
}
