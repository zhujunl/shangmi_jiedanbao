package com.yp.fastpayment.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.yp.fastpayment.R;
import com.yp.fastpayment.api.MyRetrofit;
import com.yp.fastpayment.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2020/3/20.
 */
public class IpSetActivity extends BaseActivity implements View.OnClickListener {

    ArrayList<String> iplist=new ArrayList<>(Arrays.asList("http://","https://"));
    EditText ip_edit;
    Spinner spinner;
    Button cancle,sure;
    TextView back,ip_now;
    String ip;
    LinearLayout layout1,layout2;

//    EditText et_ip_address;
//    TextView tv_set;
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
//        et_ip_address = findViewById(R.id.et_ip_address);
//        tv_set = findViewById(R.id.tv_set);
        ip_edit=findViewById(R.id.ip_eid);
        cancle=findViewById(R.id.ip_cancle);
        sure=findViewById(R.id.ip_sure);
        spinner=findViewById(R.id.ip_spinner);
        back=findViewById(R.id.ip_back);
        layout1=findViewById(R.id.ip_layout1);
        layout2=findViewById(R.id.ip_layout2);
        ip_now=findViewById(R.id.ip_now);
    }

    private void bindDefault() {
//        et_ip_address.setText(MyRetrofit.ipAddress);
//        et_ip_address.setSelection(et_ip_address.getText().toString().length());
        ip_now.setText(MyRetrofit.ipAddress);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, iplist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ip=iplist.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void registerClick() {
//        tv_set.setOnClickList
//        );
        cancle.setOnClickListener(this);
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    public void onClick(View v) {
//        switch (v.getId()){
///                clickSetIP();
//                break;
//        }
        switch (v.getId()){
            case R.id.ip_cancle:
                finish();
                break;
            case R.id.ip_sure:
                clickSetIP();
                break;
            case R.id.ip_back:
                finish();
                break;
        }
    }
//
    private void clickSetIP() {
        String edit = ip_edit.getText().toString();
        //String ipAddress= et_ip_address.getText().toString();
        if(TextUtils.isEmpty(edit)){
            showToast("请输入ip地址");
            return;
        }
//        if(!ipAddress.contains("http")){
//            showToast("请输入正确ip地址");
//            return;
//        }
        String ipAddress=ip+edit.trim();
        SharedPreferenceUtil.getInstance(this).putString("IpAddress",ipAddress);

        MyRetrofit.ipAddress = ipAddress;
//        MyRetrofit.ipAddress2 = ipAddress;

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
