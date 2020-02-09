package com.sanathbhardwaj.servicevalleydhanbad.Models;

public class CartModel {

    String Product_name;
    String Product_price;
    String Product_quantity;

    public CartModel() {
    }

    public CartModel(String product_name, String product_price, String product_quantity) {
        Product_name = product_name;
        Product_price = product_price;
        Product_quantity = product_quantity;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getProduct_price() {
        return Product_price;
    }

    public void setProduct_price(String product_price) {
        Product_price = product_price;
    }

    public String getProduct_quantity() {
        return Product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        Product_quantity = product_quantity;
    }
}
