package com.yp.fastpayment.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yp.fastpayment.R;
import com.yp.fastpayment.api.response.BranchVO;
import com.yp.fastpayment.constant.Constants;
import com.yp.fastpayment.interfaces.OnShopItemClickListenr;

/**
 * @author cp
 * @date 2019-10-10
 * description：
 */
public class SelectShopAdapter extends RecyclerView.Adapter<SelectShopAdapter.ViewHolder> {

    private static final String TAG = "SelectShopAdapter";
    OnShopItemClickListenr onShopItemClickListenr;

    public SelectShopAdapter(OnShopItemClickListenr onShopItemClickListenr) {
        this.onShopItemClickListenr = onShopItemClickListenr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_shop, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final BranchVO branchVO = Constants.branchVOList.get(position);


        holder.item_select_shop_name.setTag(position);

        holder.item_select_shop_name.setText(branchVO.getBranchName());

        holder.item_select_shop_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "v=======" + v.getTag());
                onShopItemClickListenr.onItemCallback(branchVO.getBranchId());
            }
        });



    }

    @Override
    public int getItemCount() {
        return Constants.branchVOList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_select_shop_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_select_shop_name = itemView.findViewById(R.id.item_select_shop_name);
        }
    }
}
