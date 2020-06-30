package com.yp.fastpayment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yp.fastpayment.R;
import com.yp.fastpayment.entity.GoodsInfo;
import com.yp.fastpayment.interfaces.OnShopItemClickListenr;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cp
 * @date 2019-10-10
 * description：
 */
public class OrderListDetailsAdapter extends RecyclerView.Adapter<OrderListDetailsAdapter.ViewHolder> {

    List<GoodsInfo> goodsInfos;

    public OrderListDetailsAdapter() {
        goodsInfos = new ArrayList<>();
    }

    public void setList(List<GoodsInfo> list) {
        goodsInfos.clear();
        goodsInfos.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoodsInfo goodsInfo=goodsInfos.get(position);
        holder.item_tv_details_name.setText(goodsInfo.getGoodsName());
        holder.item_tv_details_num.setText("x"+goodsInfo.getGoodsNum());
        holder.item_tv_details_price.setText("¥"+goodsInfo.getGoodsPrice());
    }

    @Override
    public int getItemCount() {
        return goodsInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_tv_details_name;
        TextView item_tv_details_num;
        TextView item_tv_details_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_tv_details_name = itemView.findViewById(R.id.item_tv_details_name);
            item_tv_details_num = itemView.findViewById(R.id.item_tv_details_num);
            item_tv_details_price = itemView.findViewById(R.id.item_tv_details_price);
        }
    }
}
