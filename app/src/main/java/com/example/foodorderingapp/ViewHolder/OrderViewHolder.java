package com.example.foodorderingapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.foodorderingapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderID, orderStatus, orderPhone, orderAddress;
    private AdapterView.OnItemClickListener mItemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        orderID = itemView.findViewById(R.id.order_id);
        orderStatus = itemView.findViewById(R.id.order_status);
        orderPhone = itemView.findViewById(R.id.order_phone);
        orderAddress = itemView.findViewById(R.id.order_address);


        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        mItemClickListener.onItemClick((AdapterView<?>) v.getParent(), v, getAdapterPosition(), v.getId());
    }
}
