package com.example.foodorderingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.foodorderingapp.Model.OrderRequest;
import com.example.foodorderingapp.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private String mDisplayPhone;

    private DatabaseReference mReference;
    private FirebaseRecyclerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        mReference = FirebaseDatabase.getInstance().getReference().child("orders");

        mRecyclerView = findViewById(R.id.listOrders);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupDisplayPhone();
        loadOrders(mDisplayPhone);
    }

    private void loadOrders(String phone) {
        mAdapter = new FirebaseRecyclerAdapter<OrderRequest, OrderViewHolder>(
                OrderRequest.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                mReference.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, OrderRequest model, int position) {
                viewHolder.orderID.setText(mAdapter.getRef(position).getKey());
                viewHolder.orderStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.orderAddress.setText(model.getAddress());
                viewHolder.orderPhone.setText(model.getPhone());
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    private String convertCodeToStatus(String status) {

        if (status.equals("0")) {
            return "Placed";
        } else if (status.equals("1")) {
            return "On your way";
        } else {
            return "Shipped";
        }
    }

    // Retrieve the display phone from the Shared Preferences
    private void setupDisplayPhone() {
        SharedPreferences prefs = getSharedPreferences(SignUpActivity.APP_PREFS, MODE_PRIVATE);
        mDisplayPhone = prefs.getString(SignUpActivity.DISPLAY_PHONE_KEY, "");
    }
}
