package com.example.foodorderingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemDetails extends AppCompatActivity {

    private TextView item_name, item_description, item_price;
    private ImageView item_image;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton btnCart;
    private ElegantNumberButton mNumberButton;

    private DatabaseReference mReference;

    private String itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        item_name = findViewById(R.id.item_name);
        item_description = findViewById(R.id.item_description);
        item_price = findViewById(R.id.item_price);
        item_image = findViewById(R.id.item_image);

        btnCart = findViewById(R.id.buttonCart);
        mNumberButton = findViewById(R.id.number_button);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_layout);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);

        mReference = FirebaseDatabase.getInstance().getReference().child("items");

        Intent intent = getIntent();
        if (intent != null){
            itemID = intent.getStringExtra("itemID");
            if(!TextUtils.isEmpty(itemID)){
                getItemDetails(itemID);
            }
        }
    }

    private void getItemDetails(String itemID) {
        mReference.child(itemID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ItemsRow itemDetails = dataSnapshot.getValue(ItemsRow.class);

                // Load Item Image
                if (itemDetails != null) {
                    Picasso.with(getBaseContext()).load(itemDetails.getImage()).into(item_image);
                    mCollapsingToolbarLayout.setTitle(itemDetails.getName());
                    item_name.setText(itemDetails.getName());
                    item_price.setText(itemDetails.getPrice());
                    item_description.setText(itemDetails.getDescription());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
