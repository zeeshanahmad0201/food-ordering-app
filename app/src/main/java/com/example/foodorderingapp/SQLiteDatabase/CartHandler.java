package com.example.foodorderingapp.SQLiteDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodorderingapp.Model.Order;

import java.util.ArrayList;
import java.util.List;

public class CartHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orders_details.db";
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_PRODUCT_ID = "productID";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
    private static final String COLUMN_DISCOUNT = "discount";
    private static final String COLUMN_PRICE = "price";

    public CartHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_orders_table = "CREATE TABLE " + TABLE_ORDERS + " ( " + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
                + COLUMN_PRODUCT_ID + " TEXT, "
                + COLUMN_PRODUCT_NAME + " TEXT, "
                + COLUMN_PRODUCT_QUANTITY + " TEXT, "
                + COLUMN_DISCOUNT + " TEXT, "
                + COLUMN_PRICE + " TEXT );";
        db.execSQL(create_orders_table);
    }

//    public List<Order> displayItemsInCart() {
//
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//
//        String[] sqlSelect = {COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_QUANTITY, COLUMN_DISCOUNT, COLUMN_PRICE};
//        queryBuilder.setTables(TABLE_ORDERS);
//        Cursor cursor = queryBuilder.query(sqLiteDatabase, sqlSelect, null, null, null, null, null);
//
//        final List<Order> result = new ArrayList<>();
//
////        do {
////            result.add(new Order(
////                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID.trim())),
////                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME.trim())),
////                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY.trim())),
////                    cursor.getString(cursor.getColumnIndex(COLUMN_DISCOUNT.trim())),
////                    cursor.getString(cursor.getColumnIndex(COLUMN_PRICE.trim()))
////            ));
////        }while(!cursor.isAfterLast());
//
//        return result;
//    }

    public List<Order> displayItemsInCart() {

        String[] sqlSelect = {COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_QUANTITY, COLUMN_DISCOUNT, COLUMN_PRICE};
        SQLiteDatabase db = getWritableDatabase();

        final List<Order> result = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_ORDERS + " WHERE 1";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            result.add(new Order(
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID.trim())),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME.trim())),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY.trim())),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DISCOUNT.trim())),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PRICE))
            ));
            cursor.moveToNext();
        }
        return result;
    }

    public void addItemsToCart(Order order) {


        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("INSERT INTO " + TABLE_ORDERS + " ( "
                        + COLUMN_PRODUCT_ID + ", "
                        + COLUMN_PRODUCT_NAME + ", "
                        + COLUMN_PRODUCT_QUANTITY + ", "
                        + COLUMN_DISCOUNT + ", "
                        + COLUMN_PRICE + " ) VALUES ('%s','%s','%s','%s','%s');",
                order.getProductID(),
                order.getProduct_name(),
                order.getProduct_quantity(),
                order.getDiscount(),
                order.getPrice());
        sqLiteDatabase.execSQL(query);
    }

    public void deleteItemsFromCart() {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "DELETE FROM " + TABLE_ORDERS;
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_ORDERS;
        db.execSQL(query);
    }
}
