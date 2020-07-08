package com.yp.fastpayment.thread;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.yp.fastpayment.api.MyCallback;
import com.yp.fastpayment.api.MyRetrofit;
import com.yp.fastpayment.api.request.OrderListRequest;
import com.yp.fastpayment.api.response.OrderListResponse;
import com.yp.fastpayment.api.response.OrderVO;
import com.yp.fastpayment.constant.Constants;
import com.yp.fastpayment.dao.OrderInfoDao;
import com.yp.fastpayment.model.OrderInfo;
import com.yp.fastpayment.ui.LoginActivity;
import com.yp.fastpayment.ui.OrderListActivity;
import com.yp.fastpayment.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import retrofit2.Call;

public class MsgSynchTask implements Runnable {


    private static final String TAG = "MsgSynchTask";
    Handler handler;

    OrderInfoDao orderDao;

    OrderListRequest request;
    
    public MsgSynchTask(Context context, Handler handler) {
        this.context = context;
        orderDao = new OrderInfoDao(context);
        this.handler = handler;

        request = new OrderListRequest();
        request.setBranchId(Constants.branchId);
        request.setDeviceId(LoginActivity.deviceId);
        request.setShopId(Constants.shopId);
        request.setPage(1);
        request.setEmployeeId(Constants.employeeId);

        Log.d(TAG, "shangmishouchiOrderList==" + GsonUtil.GsonString(request));

        Log.d(TAG, "new MsgSynchTask====");
    }

    Context context;


    @Override
    public void run() {

        Log.d(TAG, "shangmishouchiOrderList====" + request);
        MyRetrofit.getApiService().shangmishouchiGetNewOrder(request).enqueue(new MyCallback<OrderListResponse>() {

            @Override
            public void onSuccess(OrderListResponse response) {
                Log.d(TAG, "OrderListResponse==" + GsonUtil.GsonString(response));
                System.out.println("新订单:"+ GsonUtil.GsonString(response));

                if (response.getCode() == 200) {
                    List<OrderVO> orderVOList = response.getData();

                    boolean flag=false;
                    if (orderVOList != null && orderVOList.size() > 0) {

                        for (OrderVO orderVO : orderVOList) {

                            Log.d(TAG, "orderVO==" + GsonUtil.GsonString(orderVO));
                            Log.d(TAG, "orderVO item==" + GsonUtil.GsonString(orderVO.getOrderItemList()));
                            OrderInfo temp = orderDao.queryOrderByOrderNo(orderVO.getOrderNo());
                            if (temp == null) {
                                flag = true;
                                orderDao.insertData(orderVO, Constants.shopId, Constants.branchId);
                            }
                        }

                        if (flag) {
                            handler.sendEmptyMessage(1);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {

                Log.d(TAG, "loginResponse onFailure==" + t.getMessage());
                Log.d(TAG, "loginResponse onFailure==" + t.getCause());
                Log.d(TAG, "loginResponse onFailure==" + call.toString());
            }
        });

    }
}
