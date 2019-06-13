package com.example.foodorderingapp.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.foodorderingapp.Model.Order;
import com.example.foodorderingapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView item_name, item_price;
    public ImageView item_image;

    public void setItem_name(TextView item_name) {
        this.item_name = item_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        item_name = itemView.findViewById(R.id.cart_item_name);
        item_price = itemView.findViewById(R.id.cart_item_price);
        item_image = itemView.findViewById(R.id.cart_item_image);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context mContext;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.activity_cart, viewGroup, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int position) {

        TextDrawable drawable = TextDrawable.builder().buildRound("" + listData.get(position).getProduct_quantity(), Color.RED);
        cartViewHolder.item_image.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice())) * (Integer.parseInt(listData.get(position).getProduct_quantity()));
        cartViewHolder.item_price.setText(fmt.format(price));
        cartViewHolder.item_name.setText(listData.get(position).getProduct_name());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
