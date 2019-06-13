package com.example.foodorderingapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorderingapp.Model.Order;
import com.example.foodorderingapp.Model.OrderRequest;
import com.example.foodorderingapp.SQLiteDatabase.CartHandler;
import com.example.foodorderingapp.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartItems extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private TextView totalPrice;
    private Button placeOrder;

    private String mDisplayPhone;
    private String mDisplayName;

    private List<Order> cartData;
    private CartAdapter mCartAdapter;

    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_items);

        mRecyclerView = findViewById(R.id.listCart);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalPrice = findViewById(R.id.totalPrice);
        placeOrder = findViewById(R.id.placeOrder);

        loadItemsInCart();

        mReference = FirebaseDatabase.getInstance().getReference().child("orders");


        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDisplayName();
                setupDisplayPhone();

                if (!cartData.isEmpty())
                    showAlertDialog();
                else
                    Toast.makeText(CartItems.this, "Please add some items before", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Please Enter Your Address:");

        final EditText addressInput = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        addressInput.setLayoutParams(lp);
        alertDialog.setView(addressInput);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OrderRequest request = new OrderRequest(
                        mDisplayName,
                        mDisplayPhone,
                        addressInput.getText().toString(),
                        totalPrice.getText().toString().trim(),
                        cartData
                );

                mReference.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new CartHandler(getBaseContext(), null, null, 1).deleteItemsFromCart();
                Toast.makeText(CartItems.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadItemsInCart() {

        cartData = new CartHandler(this, null, null, 1).displayItemsInCart();
        mCartAdapter = new CartAdapter(cartData, this);
        mRecyclerView.setAdapter(mCartAdapter);

        // Calculate Total Price
        int total = 0;
        for (Order order : cartData)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getProduct_quantity()));
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        if (total <= 0) {
            total = 0;
        }
        totalPrice.setText(fmt.format(total));
    }

    // Retrieve the display phone from the Shared Preferences
    private void setupDisplayPhone() {
        SharedPreferences prefs = getSharedPreferences(SignUpActivity.APP_PREFS, MODE_PRIVATE);
        mDisplayPhone = prefs.getString(SignUpActivity.DISPLAY_PHONE_KEY, "");
    }

    // Retrieve the display name from the Shared Preferences
    private void setupDisplayName() {
        SharedPreferences prefs = getSharedPreferences(SignUpActivity.APP_PREFS, MODE_PRIVATE);
        mDisplayName = prefs.getString(SignUpActivity.DISPLAY_NAME_KEY, "");
    }
}
