package com.example.foodorderingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodorderingapp.Model.CategoryItems;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String mDisplayName;
    private TextView mUsername;
    private RecyclerView menuItems;

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showItemsInCartIntent = new Intent(HomeActivity.this, CartItems.class);
                startActivity(showItemsInCartIntent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        mUsername = headerView.findViewById(R.id.username);
        setupDisplayName();
        mUsername.setText(mDisplayName);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Setting up RecyclerView
        menuItems = findViewById(R.id.menuItems);
        menuItems.setLayoutManager(new LinearLayoutManager(this));
        menuItems.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child("category");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<CategoryItems, CategoryItemsHolder> FBRA = new FirebaseRecyclerAdapter<CategoryItems, CategoryItemsHolder>(
                CategoryItems.class,
                R.layout.menu_item,
                CategoryItemsHolder.class,
                mReference
        ) {
            @Override
            protected void populateViewHolder(CategoryItemsHolder viewHolder, CategoryItems model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setImage(getApplicationContext(), model.getUrl());

                final String categoryID = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent itemIntent = new Intent(HomeActivity.this, ItemsActivity.class);
                        itemIntent.putExtra("categoryID", categoryID);
                        startActivity(itemIntent);
                    }
                });
            }
        };
        menuItems.setAdapter(FBRA);
    }


    public static class CategoryItemsHolder extends RecyclerView.ViewHolder {


        View mView;

        public CategoryItemsHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView categoryName = mView.findViewById(R.id.categoryName);
            categoryName.setText(name);
        }

        public void setImage(Context context, String image) {
            ImageView categoryImage = mView.findViewById(R.id.categoryImage);
            Picasso.with(context).load(image).into(categoryImage);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Go to home activity
            //Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(getIntent());
        } else if (id == R.id.nav_cart) {
            Intent cartActivity = new Intent(HomeActivity.this, CartItems.class);
            startActivity(cartActivity);
        }else if (id == R.id.nav_orders) {
            Intent orderActivity = new Intent(HomeActivity.this, OrderStatus.class);
            startActivity(orderActivity);
        }else if (id == R.id.nav_signOut) {
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Retrieve the display name from the Shared Preferences
    private void setupDisplayName() {
        SharedPreferences prefs = getSharedPreferences(SignUpActivity.APP_PREFS, MODE_PRIVATE);
        mDisplayName = prefs.getString(SignUpActivity.DISPLAY_NAME_KEY, "");
    }


}
