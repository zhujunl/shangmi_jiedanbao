package com.yp.fastpayment.ui;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;

import com.yp.fastpayment.R;
import com.yp.fastpayment.adapter.SelectShopAdapter;
import com.yp.fastpayment.api.MyCallback;
import com.yp.fastpayment.api.MyRetrofit;
import com.yp.fastpayment.api.request.OrderListRequest;
import com.yp.fastpayment.api.response.BranchVO;
import com.yp.fastpayment.api.response.OrderListResponse;
import com.yp.fastpayment.api.response.OrderVO;
import com.yp.fastpayment.constant.Constants;
import com.yp.fastpayment.dao.OrderInfoDao;
import com.yp.fastpayment.interfaces.OnShopItemClickListenr;
import com.yp.fastpayment.model.OrderInfo;
import com.yp.fastpayment.util.GsonUtil;
import com.yp.fastpayment.util.SharedPreferenceUtil;

import java.util.List;

/**
 * @author cp
 * @date 2019-10-10
 * description：
 */
public class SelectShopActivity extends BaseActivity implements OnShopItemClickListenr {

    private static final String TAG = "SelectShopActivity";
    RecyclerView recyclerView;
    SelectShopAdapter selectShopAdapter;

    OrderInfoDao orderDao;

    @Override
    protected int layoutId() {
        return R.layout.activity_select_shop;
    }

    @Override
    protected void initData() {
        if(!SharedPreferenceUtil.getInstance(this).getString("branchName").equals("")&&!"".equals(SharedPreferenceUtil.getInstance(this).getString("branchName"))){
            Constants.branchId=SharedPreferenceUtil.getInstance(this).getInt("branchId");
            Constants.branchName=SharedPreferenceUtil.getInstance(this).getString("branchName");
            startActivity(new Intent(mContext, OrderListActivity.class));
        }
        orderDao = new OrderInfoDao(this);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        selectShopAdapter = new SelectShopAdapter(this);
        recyclerView.setAdapter(selectShopAdapter);
    }

    @Override
    public void onItemCallback(int pos) {
        Constants.branchId = pos;
        SharedPreferenceUtil.getInstance(this).putInt("branchId",Constants.branchId);
        for (BranchVO branchVO : Constants.branchVOList) {
            if (branchVO.getBranchId().intValue() == pos) {
                SharedPreferenceUtil.getInstance(this).putString("branchName",branchVO.getBranchName());
                Constants.branchName = branchVO.getBranchName();
            }
        }


        final OrderListRequest request = new OrderListRequest();
        request.setBranchId(Constants.branchId);
        request.setDeviceId(LoginActivity.deviceId);
        request.setShopId(Constants.shopId);
        request.setPage(1);
        request.setEmployeeId(Constants.employeeId);

       // Toast.makeText(this, GsonUtil.GsonString(request), Toast.LENGTH_LONG).show();

        Log.d(TAG, "shangmishouchiOrderList==" + GsonUtil.GsonString(request));


       MyRetrofit.getApiService().shangmishouchiOrderList(request).enqueue(new MyCallback<OrderListResponse>() {
 //       MyRetrofit.getApiService4().shangmishouchiOrderList(request).enqueue(new MyCallback<OrderListResponse>() {
            @Override
            public void onSuccess(OrderListResponse response) {
                Log.d(TAG, "OrderListResponse==" + GsonUtil.GsonString(response));
                if (response.getCode() == 200) {
                    List<OrderVO> orderVOList = response.getData();

                    if (orderVOList == null || orderVOList.size() == 0) {
                        showToast("分店暂无订单");
                    }
                    Constants.orderVOList = orderVOList;
                    for (OrderVO orderVO : orderVOList) {

                        Log.d(TAG, "orderVO==" + GsonUtil.GsonString(orderVO));
                        Log.d(TAG, "orderVO item==" + GsonUtil.GsonString(orderVO.getOrderItemList()));
                        OrderInfo temp = orderDao.queryOrderByOrderNo(orderVO.getOrderNo());
                        if (temp == null) {
                            orderDao.insertData(orderVO, Constants.shopId, Constants.branchId);
                        }
                    }

                    startActivity(new Intent(mContext, OrderListActivity.class));
                }else {
                    showToast("分店暂无订单");
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
}
