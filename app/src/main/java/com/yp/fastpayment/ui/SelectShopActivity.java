package com.yp.fastpayment.ui;

import android.content.Intent;
import android.util.Log;

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
        for (BranchVO branchVO : Constants.branchVOList) {
            if (branchVO.getBranchId().intValue() == pos) {
                Constants.branchName = branchVO.getBranchName();
            }
        }


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

                    if (orderVOList == null || orderVOList.size() == 0) {
                        showToast("分店暂无订单");
                        return;
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
