package com.example.foodorderingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodorderingapp.Model.ItemsRow;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ItemsActivity extends AppCompatActivity {

    private RecyclerView itemsList;

    private DatabaseReference mReference;

    private String categoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Intent intent = getIntent();
        if (intent != null) {
            categoryID = intent.getStringExtra("categoryID");
        }

        itemsList = findViewById(R.id.itemsList);
        itemsList.setHasFixedSize(true);
        itemsList.setLayoutManager(new LinearLayoutManager(this));

        mReference = FirebaseDatabase.getInstance().getReference().child("items");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ItemsRow, ItemsViewHolder> FBRA = new FirebaseRecyclerAdapter<ItemsRow, ItemsViewHolder>(
                ItemsRow.class,
                R.layout.items_row,
                ItemsViewHolder.class,
                mReference.orderByChild("menuID").equalTo(categoryID)
        ) {
            @Override
            protected void populateViewHolder(ItemsViewHolder viewHolder, ItemsRow model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setImage(model.getImage(), getApplicationContext());
                final String itemID = getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent itemDetailsIntent = new Intent(ItemsActivity.this, ItemDetails.class);
                        itemDetailsIntent.putExtra("itemID", itemID);
                        startActivity(itemDetailsIntent);
                    }
                });
            }
        };

        itemsList.setAdapter(FBRA);
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView itemName = mView.findViewById(R.id.itemName);
            itemName.setText(name);
        }

        public void setImage(String url, Context context){
            ImageView itemImage = mView.findViewById(R.id.itemImage);
            Picasso.with(context).load(url).into(itemImage);
        }
    }
}
