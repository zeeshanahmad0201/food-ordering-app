package com.example.foodorderingapp.Model;

public class Order {

    private String productID, product_name, product_quantity, discount, price;

    public Order() {
    }

    public Order(String productID, String product_name, String product_quantity,String discount, String price) {
        this.productID = productID;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.discount = discount;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
