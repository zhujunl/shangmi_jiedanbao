package com.yp.fastpayment.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.yp.fastpayment.api.MyCallback;
import com.yp.fastpayment.api.MyRetrofit;
import com.yp.fastpayment.api.request.OrderDelRequest;
import com.yp.fastpayment.api.request.OrderListRequest;
import com.yp.fastpayment.api.request.PeriodRequest;
import com.yp.fastpayment.api.response.OrderDelResponse;
import com.yp.fastpayment.api.response.OrderDelVO;
import com.yp.fastpayment.api.response.OrderListResponse;
import com.yp.fastpayment.api.response.OrderVO;
import com.yp.fastpayment.constant.Constants;
import com.yp.fastpayment.dao.OrderInfoDao;
import com.yp.fastpayment.model.OrderInfo;
import com.yp.fastpayment.ui.LoginActivity;
import com.yp.fastpayment.ui.OrderListActivity;
import com.yp.fastpayment.util.BluetoothUtil;
import com.yp.fastpayment.util.ESCUtil;
import com.yp.fastpayment.util.GsonUtil;
import com.yp.fastpayment.util.QrcodeUtil;
import com.yp.fastpayment.util.SharedPreferenceUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MsgSynchTask implements Runnable {


    private static final String TAG = "MsgSynchTask";
    Handler handler;

    PeriodRequest periodRequest;
    OrderDelRequest orderDelRequest;
    List<String> CancleList=new ArrayList<>();

    OrderInfoDao orderDao;
    OrderListRequest request;
    String orderNo;
    
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


        periodRequest=new PeriodRequest();
        periodRequest.setBranchId(Constants.branchId);
        periodRequest.setShopId(Constants.shopId);

        orderDelRequest=new OrderDelRequest();
        orderDelRequest.setPage_num(1);
        orderDelRequest.setPage_size(9999);
        orderDelRequest.setParams(periodRequest);
        orderDelRequest.setSql_id("GET-CANCLE-RESERVER-ORDERNOS");

        Log.d(TAG, "shangmishouchiOrderList==" + GsonUtil.GsonString(request));

        Log.d(TAG, "new MsgSynchTask====");
    }

    Context context;


    @Override
    public void run() {

        /**
         * 获取新订单
         */
        Log.d(TAG, "shangmishouchiOrderList====" + request);
        MyRetrofit.getApiService().shangmishouchiGetNewOrder(request).enqueue(new Callback<OrderListResponse>() {

            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                Log.d(TAG, "MsgSynchTaskOrderListResponse==" + GsonUtil.GsonString(response));
                System.out.println("新订单:"+ GsonUtil.GsonString(response));

                if (response.code() == 200) {
                    List<OrderVO> orderVOList = response.body().getData();

                    boolean flag=false;
                    if (orderVOList != null && orderVOList.size() > 0) {

                        for (OrderVO orderVO : orderVOList) {

                            Log.d(TAG, "orderVO==" + GsonUtil.GsonString(orderVO));
                            Log.d(TAG, "orderVO item==" + GsonUtil.GsonString(orderVO.getOrderItemList()));
                            OrderInfo temp = orderDao.queryOrderByOrderNo(orderVO.getOrderNo());
                            if (temp == null) {
                                flag = true;
                                orderNo=orderVO.getOrderNo();
                                Log.d("orderoooo=====",orderVO.toString());
                                orderDao.insertData(orderVO, Constants.shopId, Constants.branchId);
                            }
                        }
                        if (flag) {
                            if(Constants.isAutoPrint==true){
                                List<OrderInfo> orderInfo=orderDao.queryrNo(orderNo);
                                for (OrderInfo i :orderInfo){
                                    String meanlname=SharedPreferenceUtil.getInstance(context).getString("mealname");
                                    Log.d("mealname====",meanlname);
                                    Log.d("i=========",GsonUtil.GsonString(i));
                                    if(meanlname.equals("全部")){
                                        doPrintOrder(i);
                                    }else {
                                        if(i.getMealHourConfigName().equals(meanlname)){
                                            doPrintOrder(i);
                                        }
                                    }
                                }
                            }
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


/**
 * 获取被取消的订单
 */
        Log.d("orderDelRequest=====",orderDelRequest.toString());

        MyRetrofit.getApiService().GetDeleteOrder(orderDelRequest).enqueue(new Callback<OrderDelResponse>() {
            @Override
            public void onResponse(Call<OrderDelResponse> call, Response<OrderDelResponse> response) {
                Log.d("GetDeleteOrder=======",GsonUtil.GsonString(response));
                if(response.code()==200){
                    CancleList.clear();
                    List<OrderDelVO.Data> data=response.body().getData().getResults();
                    if(data!=null){
                        for (OrderDelVO.Data i : data){
                            Log.d("delorderno====",i.getOrder_no());
                            CancleList.add(i.getOrder_no());
                        }
                    }
                    if (CancleList.size()>0){
                        for (String i :CancleList){
                            orderDao.deleteByOrderno(i);
                            handler.sendEmptyMessage(2);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDelResponse> call, Throwable t) {
                Log.d(TAG, "GetDeleteOrder onFailure==" + t.getMessage());
                Log.d(TAG, "GetDeleteOrder onFailure==" + t.getCause());
                Log.d(TAG, "GetDeleteOrder onFailure==" + call.toString());
            }
        });

    }

    public void doPrintOrder(OrderInfo orderInfo) {

        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {
            return;
        }
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {
            return;
        }

        // 3: Generate a order data
        //插入二维码操作
        // "mealCode": 1234,
        //1234
        //{"mealCode": 1234}
        byte[] data = new byte[0];
        try {
            byte[] code = TextUtils.isEmpty(orderInfo.getMealCode()) ? null : QrcodeUtil.draw2PxPoint(QrcodeUtil.generateBitmap(new JSONObject().put("mealCode", orderInfo.getMealCode()).toString(), 200, 200));
            data = ESCUtil.generateMockData(orderInfo, code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4: Using InnerPrinter print data
        BluetoothSocket socket = null;
        try {
            socket = BluetoothUtil.getSocket(device);

            BluetoothUtil.sendData(data, socket);
        } catch (IOException e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        orderInfo.setPrintState(1);

        orderDao.updatePrintState(1, orderInfo.getOrderNo());

    }
}
