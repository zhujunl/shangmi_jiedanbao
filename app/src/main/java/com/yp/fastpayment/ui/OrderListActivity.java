package com.yp.fastpayment.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yp.fastpayment.R;
import com.yp.fastpayment.adapter.OrderListAdapter;
import com.yp.fastpayment.constant.Constants;
import com.yp.fastpayment.dao.OrderInfoDao;
import com.yp.fastpayment.model.OrderInfo;
import com.yp.fastpayment.interfaces.OnOrderItemClickListenr;
import com.yp.fastpayment.thread.MsgSynchTask;
import com.yp.fastpayment.util.BluetoothUtil;
import com.yp.fastpayment.util.ESCUtil;
import com.yp.fastpayment.util.GsonUtil;
import com.yp.fastpayment.util.Jc_Utils;
import com.yp.fastpayment.util.QrcodeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author jay
 * @date 2019-10-10
 * description：
 */
public class OrderListActivity extends BaseActivity implements View.OnClickListener, OnOrderItemClickListenr, OnItemClickListener {
    TextView tv_merchant_name;
    TextView tv_shop_name;
    SwipeRecyclerView swipe_recycler_view;
    LinearLayout ll_shop_name;
    OrderListAdapter orderListAdapter;
    private static final String TAG = "OrderListActivity";
    List<OrderInfo> orderInfoList = new ArrayList<>();
    OrderInfoDao orderDao;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                playMusic(1);
                updateOrderInfo();
                return;
            }
        }
    };

    @Override
    protected int layoutId() {
        return R.layout.activity_order_list;
    }

    ScheduledExecutorService scheduledExecutorServiceWithMsg;

    @Override
    protected void initData() {
        orderDao = new OrderInfoDao(this);

        scheduledExecutorServiceWithMsg = Executors.newScheduledThreadPool(10);
        scheduledExecutorServiceWithMsg.scheduleWithFixedDelay(new MsgSynchTask(this, handler),
                30, 5, TimeUnit.SECONDS);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduledExecutorServiceWithMsg.shutdown();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        updateOrderInfo();
    }

    public void updateOrderInfo() {
        Log.d(TAG, "orderDao.queryOrderList, shopId==" + Constants.shopId + ", branchId==" + Constants.branchId);
        orderInfoList = orderDao.query(Constants.shopId, Constants.branchId);

        Log.d(TAG, "orderInfoList==" + GsonUtil.GsonString(orderInfoList));
        orderListAdapter.setOrderInfoList(orderInfoList);
    }

    @Override
    protected void initView() {
        initPop();
        ll_shop_name = findViewById(R.id.ll_shop_name);
        tv_merchant_name = findViewById(R.id.tv_merchant_name);
        tv_shop_name = findViewById(R.id.tv_shop_name);
        ll_shop_name.setOnClickListener(this);
        swipe_recycler_view = findViewById(R.id.swipe_recycler_view);
        swipe_recycler_view.setLayoutManager(new LinearLayoutManager(mContext));
        orderListAdapter = new OrderListAdapter(this, this);
        swipe_recycler_view.setOnItemMenuClickListener(mItemMenuClickListener); // Item的Menu点击。
//        swipe_recycler_view.setOnItemMenuClickListener(mItemMenuClickListener); // Item的Menu点击。
        swipe_recycler_view.setSwipeMenuCreator(mSwipeMenuCreator); // 菜单创建器。
        swipe_recycler_view.setOnItemClickListener(this);
        swipe_recycler_view.setAdapter(orderListAdapter);

        tv_merchant_name.setText(Constants.shopName);
        tv_shop_name.setText(Constants.branchName);

        Log.d(TAG, "orderInfoList============================" + GsonUtil.GsonString(orderInfoList));

    }

    /**
     * 菜单创建器。
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            int width = AutoSizeUtils.dp2px(mContext, 106);

            SwipeMenuItem printItem = new SwipeMenuItem(mContext).setBackgroundColor(
                    getResources().getColor(R.color.print_bg))
                    .setText("打印")
                    .setTextColor(Color.WHITE)
                    .setTextSize(AutoSizeUtils.sp2px(mContext, 12))
                    .setWidth(width)
                    .setHeight(height);

            SwipeMenuItem printAgainItem = new SwipeMenuItem(mContext).setBackgroundColor(
                    getResources().getColor(R.color.print_again_bg))
                    .setText("再次打印")
                    .setTextColor(Color.WHITE)
                    .setTextSize(AutoSizeUtils.sp2px(mContext, 11))
                    .setWidth(width)
                    .setHeight(height);
            //根据打印状态显示
            if (orderInfoList.get(position).getPrintState() == 1) {
                swipeRightMenu.addMenuItem(printAgainItem);
            } else {
                swipeRightMenu.addMenuItem(printItem);
            }

            SwipeMenuItem detailsItem = new SwipeMenuItem(mContext).setBackgroundColor(
                    getResources().getColor(R.color.details_bg))
                    .setText("查看详情")
                    .setTextColor(Color.WHITE)
                    .setTextSize(AutoSizeUtils.sp2px(mContext, 11))
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(detailsItem);// 添加一个按钮到右侧侧菜单。

        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {
                    printOrder(position);
                } else if (menuPosition == 1) {
                    openDetails(position);
                }
            }
        }
    };

    /**
     * 打印第几个订单
     *
     * @param pos
     */
    private void printOrder(int pos) {
//        showToast("打印list第" + pos + "项");
        doPrintOrder(orderInfoList.get(pos));
    }

    private void openDetails(int pos) {

        Intent intent = new Intent(mContext, OrderDetailsActivity.class);
        Constants.curOrderInfo = orderInfoList.get(pos);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shop_name:
                popupWindow.showAsDropDown(ll_shop_name, 20, 20);
                bgAlpha(0.5f);
                break;
            case R.id.tv_change_user:
                popupWindow.dismiss();
                startActivity(new Intent(mContext, SelectShopActivity.class));
                finish();
                break;
            case R.id.tv_login_out:
                popupWindow.dismiss();
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemCallback(int pos) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        bgAlpha(1);
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        openDetails(adapterPosition);
    }

    PopupWindow popupWindow;

    void initPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_layout, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.pop_layout, null));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失时使背景不透明
                bgAlpha(1f);
            }
        });

        view.findViewById(R.id.tv_change_user).setOnClickListener(this);
        view.findViewById(R.id.tv_login_out).setOnClickListener(this);
    }

    private void bgAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    @Override
    public void onBackPressed() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }


    public void doPrintOrder(OrderInfo orderInfo) {

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
        //插入二维码操作
        // "mealCode": 1234,
        //1234
        //{"mealCode": 1234}
        byte[] data = new byte[0];
        try {
            data = ESCUtil.generateMockData(orderInfo,
                    QrcodeUtil.draw2PxPoint(QrcodeUtil.generateBitmap(new JSONObject().put("mealCode",orderInfo.getSerial()).toString(),200,200)));
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

        orderListAdapter.setOrderInfoList(orderInfoList);
    }


    MediaPlayer mediaPlayer;

    void playMusic(int type) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
        mediaPlayer.reset();
        if (type == 1) {
            try {
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.neworder));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
