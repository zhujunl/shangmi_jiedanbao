package com.yp.fastpayment.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yp.fastpayment.R;
import com.yp.fastpayment.api.MyCallback;
import com.yp.fastpayment.api.MyRetrofit;
import com.yp.fastpayment.api.request.InitRequest;
import com.yp.fastpayment.api.request.OrderListRequest;
import com.yp.fastpayment.api.response.BranchVO;
import com.yp.fastpayment.api.response.InitResponse;
import com.yp.fastpayment.api.response.OrderListResponse;
import com.yp.fastpayment.api.response.OrderVO;
import com.yp.fastpayment.constant.Constants;
import com.yp.fastpayment.dao.OrderInfoDao;
import com.yp.fastpayment.dao.ShopConfigDao;
import com.yp.fastpayment.model.OrderInfo;
import com.yp.fastpayment.model.ShopConfig;
import com.yp.fastpayment.util.GsonUtil;
import com.yp.fastpayment.util.Jc_Utils;
import com.yp.fastpayment.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;

/**
 * @author cp
 * @date 2019-10-10
 * description：
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    EditText edit_user_account, edit_user_psw;
    public static String deviceId = "";

    ShopConfigDao shopConfigDao;
    OrderInfoDao orderInfoDao;
    TextView tv_ip;

    final static int COUNTS = 5;// 点击次数
    final static long DURATION = 3 * 1000;// 规定有效时间
    long[] mHits = new long[COUNTS];

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        shopConfigDao = new ShopConfigDao(this);
        orderInfoDao = new OrderInfoDao(this);

        ShopConfig shopConfig = shopConfigDao.query();
        if (shopConfig != null) {
            loginAdmin();
        }
    }

    @Override
    protected void initView() {
        edit_user_account = findViewById(R.id.edit_user_account);
        edit_user_psw = findViewById(R.id.edit_user_psw);
        tv_ip = findViewById(R.id.tv_ip);
        findViewById(R.id.btn_login_user).setOnClickListener(onClickListener);

        tv_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continuousClick(COUNTS, DURATION);
            }
        });

        String ipAddress = SharedPreferenceUtil.getInstance(this).getString("IpAddress");

        if (ipAddress != null && !"".equals(ipAddress)) {
            MyRetrofit.ipAddress = ipAddress;
        }

        checkBluetoothPermission();

        WifiManager wifiManager = (WifiManager) getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String mac = wifiInfo.getMacAddress();

//        deviceId = mac;
//        System.out.println("mac1:"+mac);
        deviceId = Jc_Utils.getMacFromHardware(this);
        System.out.println("mac2:" + deviceId);
    }

    private void continuousClick(int COUNTS, long DURATION) {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //为数组最后一位赋值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
            mHits = new long[COUNTS];//重新初始化数组
//            连续点击了5下
            Intent intent = new Intent(LoginActivity.this, IpSetActivity.class);
            startActivity(intent);
        }
    }

    /*public void login(View view){
        startActivity(new Intent(mContext,SelectShopActivity.class));
        finish();
    }*/

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loginAdmin();
        }
    };


    void loginAdmin() {

        String user_account = edit_user_account.getText().toString().trim();
        String user_psw = edit_user_psw.getText().toString().trim();

        if (user_account == null || "".equals(user_account)) {
            ShopConfig shopConfig = shopConfigDao.query();
            if (shopConfig != null) {
                user_account = shopConfig.getUsername();
                user_psw = shopConfig.getPassword();
            }
        }

        /*if (user_account == null || "".equals(user_account) || "".equals(user_account.trim())) {
            showToast("用户名必填");
            return;
        }

        if (user_psw == null || "".equals(user_psw) || "".equals(user_psw.trim())) {
            showToast("密码必填");
            return;
        }*/

        final InitRequest loginRequest = new InitRequest();
        loginRequest.setDeviceId(LoginActivity.deviceId);
        loginRequest.setUsername(user_account);
        loginRequest.setPassword(user_psw);

//        loginRequest.setUsername("13811223344");
//        loginRequest.setPassword("000000");

        Log.d(TAG, "loginRequest==" + GsonUtil.GsonString(loginRequest));

        MyRetrofit.getApiService().shangmishouchiInit(loginRequest).enqueue(new MyCallback<InitResponse>() {

            @Override
            public void onSuccess(InitResponse loginResponse) {
                Toast.makeText(LoginActivity.this,loginResponse.toString(),Toast.LENGTH_SHORT).show();

                if (loginResponse.getCode() == 200) {
                    List<BranchVO> branchVOList = loginResponse.getData().getBranchList();

                    Constants.shopId = loginResponse.getData().getShopId();
                    Constants.shopName = loginResponse.getData().getShopName();
                    Constants.employeeId = loginResponse.getData().getEmployeeId();

                    shopConfigDao.insertData(Constants.shopId,
                            loginResponse.getData().getCashierDeskId(),
                            loginResponse.getData().getBranchId(),
                            loginResponse.getData().getShopName(),
                            loginRequest.getUsername(), loginRequest.getPassword());
                    Log.d(TAG, "shopConfigDao insert==" + GsonUtil.GsonString(loginResponse));
                    if (branchVOList != null) {
                        if (branchVOList.size() == 1) {
                            Constants.branchId = branchVOList.get(0).getBranchId();
                            Constants.branchName = branchVOList.get(0).getBranchName();

                            Constants.branchVOList = new ArrayList<>();
                            BranchVO branchVO = new BranchVO();
                            branchVO.setBranchId(Constants.branchId);
                            branchVO.setBranchName(Constants.branchName);

                            Constants.branchVOList.add(branchVO);

                            startActivity(new Intent(mContext, SelectShopActivity.class));
                            Log.d(TAG, "branchVOList.size() == 1");
                        } else {
                            Constants.branchVOList = branchVOList;
                            startActivity(new Intent(mContext, SelectShopActivity.class));
                        }
                    } else {
                        Constants.branchId = loginResponse.getData().getBranchId();
                        Constants.branchName = loginResponse.getData().getShopName();
                        Constants.branchVOList = new ArrayList<>();
                        BranchVO branchVO = new BranchVO();
                        branchVO.setBranchId(Constants.branchId);
                        branchVO.setBranchName(Constants.branchName);

                        Constants.branchVOList.add(branchVO);
                        startActivity(new Intent(mContext, SelectShopActivity.class));
                        Log.d(TAG, "branchVOList.size() === 1");
                    }

                    ShopConfig shopConfig = shopConfigDao.query();
                    Log.d(TAG, "shopConfigDao query==" + GsonUtil.GsonString(shopConfig));
                } else {
                    showToast(loginResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<InitResponse> call, Throwable t) {

                Log.d(TAG, "loginResponse onFailure==" + t.getMessage());
                showToast("网络异常");

            }
        });


    }


    private void toOrderListPage() {

        final OrderListRequest request = new OrderListRequest();
        request.setBranchId(Constants.branchId);
        request.setDeviceId(LoginActivity.deviceId);
        request.setShopId(Constants.shopId);
        request.setPage(1);
        request.setEmployeeId(Constants.employeeId);

        Log.d(TAG, "shangmishouchiOrderList==" + GsonUtil.GsonString(request));


        MyRetrofit.getApiService().shangmishouchiOrderList(request).enqueue(new MyCallback<OrderListResponse>() {

            @Override
            public void onSuccess(OrderListResponse response) {
                Log.d(TAG, "OrderListResponse==" + GsonUtil.GsonString(response));

                if (response.getCode() == 200) {
                    List<OrderVO> orderVOList = response.getData();

                    Constants.orderVOList = orderVOList;
                    for (OrderVO orderVO : orderVOList) {

                        Log.d(TAG, "orderVO==" + GsonUtil.GsonString(orderVO));
                        Log.d(TAG, "orderVO item==" + GsonUtil.GsonString(orderVO.getOrderItemList()));
                        OrderInfo temp = orderInfoDao.queryOrderByOrderNo(orderVO.getOrderNo());
                        if (temp == null) {
                            orderInfoDao.insertData(orderVO, Constants.shopId, Constants.branchId);
                        }
                    }

                    startActivity(new Intent(mContext, OrderListActivity.class));
                }
            }

            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {

                Log.d(TAG, "loginResponse onFailure==" + t.getMessage());
                Log.d(TAG, "loginResponse onFailure==" + t.getCause());
                Log.d(TAG, "loginResponse onFailure==" + call.toString());
                showToast("网络异常");
//                super.onFailure(call, t);
            }
        });


    }


    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION};
    final private int REQUEST_PERMISSION_CODE = 20001;

    /*
  校验蓝牙权限
 */
    private void checkBluetoothPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else {
                Log.d(TAG, "无需授权  ");
            }
        } else {//小于23版本直接使用
            Log.d(TAG, "checkBluetoothPermission: 小于23版本直接使用");
        }
    }
}
