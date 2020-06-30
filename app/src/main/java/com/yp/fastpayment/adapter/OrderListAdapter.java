package com.yp.fastpayment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.yp.fastpayment.R;
import com.yp.fastpayment.api.response.OrderVO;
import com.yp.fastpayment.model.OrderInfo;
import com.yp.fastpayment.interfaces.OnOrderItemClickListenr;
import com.yp.fastpayment.util.PriceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cp
 * @date 2019-10-10
 * description：
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    Context context;

    OnOrderItemClickListenr iSeleteShopListen;
    List<OrderInfo> orderInfoList;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public OrderListAdapter(Context context, OnOrderItemClickListenr iSeleteShopListen) {
        this.context = context;
        this.iSeleteShopListen = iSeleteShopListen;
        orderInfoList=new ArrayList<>();
    }

    public void setOrderInfoList(List<OrderInfo> orderList) {
        orderInfoList.clear();
        orderInfoList.addAll(orderList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderInfo orderInfo=orderInfoList.get(position);
        holder.item_tv_order_serial_num.setText("取餐号 " + orderInfo.getSerial());
        holder.item_tv_order_num.setText("订单号： " + orderInfo.getOrderNo());
        holder.item_tv_order_price.setText("¥" + PriceUtil.changeF2Y(orderInfo.getRealfee()));
        holder.item_tv_order_date.setText(simpleDateFormat.format(orderInfo.getPaytime()));
        if (orderInfo.getPrintState()==1){
            holder.item_tv_order_print_state.setText("已打印");
            holder.item_tv_order_print_state.setTextColor(context.getResources().getColor(R.color.print_text));
            holder.item_tv_order_print_state.setBackgroundResource(R.drawable.printed_bg);
        }else {
            holder.item_tv_order_print_state.setText("未打印");
            holder.item_tv_order_print_state.setTextColor(context.getResources().getColor(R.color.white));
            holder.item_tv_order_print_state.setBackgroundResource(R.drawable.unprint_bg);
        }
    }

    @Override
    public int getItemCount() {
        return orderInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_tv_order_serial_num;
        TextView item_tv_order_num;
        TextView item_tv_order_price;
        TextView item_tv_order_date;
        TextView item_tv_order_print_state;
        ConstraintLayout ll_order_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_tv_order_serial_num = itemView.findViewById(R.id.item_tv_order_serial_num);
            item_tv_order_num = itemView.findViewById(R.id.item_tv_order_num);
            item_tv_order_price = itemView.findViewById(R.id.item_tv_order_price);
            item_tv_order_date = itemView.findViewById(R.id.item_tv_order_date);
            item_tv_order_print_state = itemView.findViewById(R.id.item_tv_order_print_state);
            ll_order_layout = itemView.findViewById(R.id.ll_order_layout);
        }
    }
}
