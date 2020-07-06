package com.yp.fastpayment.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yp.fastpayment.R;
import com.yp.fastpayment.adapter.OrderListDetailsAdapter;
import com.yp.fastpayment.api.response.MeshOrderItemVO;
import com.yp.fastpayment.constant.Constants;
import com.yp.fastpayment.dao.OrderInfoDao;
import com.yp.fastpayment.entity.GoodsInfo;
import com.yp.fastpayment.model.OrderInfo;
import com.yp.fastpayment.util.BluetoothUtil;
import com.yp.fastpayment.util.ESCUtil;
import com.yp.fastpayment.util.PriceUtil;
import com.yp.fastpayment.util.QrcodeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cp
 * @date 2019-10-11
 * description：订单详情
 */
public class OrderDetailsActivity extends BaseActivity {
    RecyclerView recyclerView;
    OrderListDetailsAdapter orderListDetailsAdapter;

    TextView tv_order_details_title;
    TextView tv_details_order_num;
    TextView tv_details_order_date;
    TextView tv_details_order_user_name;
    TextView tv_details_order_phone;
    TextView tv_details_total_num;
    TextView tv_details_note;
    TextView tv_details_subtotal_price;
    TextView tv_details_reduction_price;
    TextView tv_details_total_price;

    OrderInfoDao orderDao;

    @Override
    protected int layoutId() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void initData() {
        orderDao = new OrderInfoDao(this);

        //标题
        tv_order_details_title.setText("取餐号:" + Constants.curOrderInfo.getSerial());
        //订单号
        tv_details_order_num.setText(Constants.curOrderInfo.getOrderNo());

        tv_details_order_date.setText(OrderInfoDao.simpleDateFormat.format(Constants.curOrderInfo.getPaytime()));

        tv_details_order_user_name.setText(Constants.curOrderInfo.getCustomerName());

        tv_details_order_phone.setText(Constants.curOrderInfo.getCustomerPhone());
        tv_details_note.setText(Constants.curOrderInfo.getNote());

        tv_details_total_num.setText("x" + Constants.curOrderInfo.getOrderItemList().size());

        tv_details_subtotal_price.setText("¥" + PriceUtil.changeF2Y(Constants.curOrderInfo.getTotalfee()));
        tv_details_reduction_price.setText("¥" + PriceUtil.changeF2Y((Constants.curOrderInfo.getTotalfee() - Constants.curOrderInfo.getRealfee())));
        tv_details_total_price.setText("¥" + PriceUtil.changeF2Y(Constants.curOrderInfo.getRealfee()));


        List<GoodsInfo> list=new ArrayList<>();
        for (MeshOrderItemVO meshOrderItemVO : Constants.curOrderInfo.getOrderItemList()) {

            list.add(new GoodsInfo(meshOrderItemVO.getProductName(),meshOrderItemVO.getQuantity(),PriceUtil.changeF2Y(meshOrderItemVO.getPrice())));
        }
        orderListDetailsAdapter.setList(list);
    }

    @Override
    protected void initView() {
        tv_order_details_title = findViewById(R.id.tv_order_details_title);
        tv_details_order_num = findViewById(R.id.tv_details_order_num);
        tv_details_order_date = findViewById(R.id.tv_details_order_date);
        tv_details_order_user_name = findViewById(R.id.tv_details_order_user_name);
        tv_details_order_phone = findViewById(R.id.tv_details_order_phone);
        tv_details_total_num = findViewById(R.id.tv_details_total_num);
        tv_details_subtotal_price = findViewById(R.id.tv_details_subtotal_price);
        tv_details_reduction_price = findViewById(R.id.tv_details_reduction_price);
        tv_details_total_price = findViewById(R.id.tv_details_total_price);
        tv_details_note = findViewById(R.id.tv_details_note);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        orderListDetailsAdapter = new OrderListDetailsAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(orderListDetailsAdapter);
    }

    public void back(View view) {
        finish();
    }

    public void print(View view) {
        doPrintOrder(Constants.curOrderInfo);
    }

    public void doPrintOrder(OrderInfo orderInfo) {
        orderDao.updatePrintState(1, orderInfo.getOrderNo());

        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
            showToast("正在打开蓝牙");
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {
            Toast.makeText(getBaseContext(), "Please Make Sure Bluetooth have InnterPrinter!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // 3: Generate a order data
        byte[] data = new byte[0];
        try {
            data = ESCUtil.generateMockData(orderInfo,
                    QrcodeUtil.draw2PxPoint(QrcodeUtil.generateBitmap(orderInfo.getSerial(),200,200)));
        } catch (IOException e) {
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



    }
}
