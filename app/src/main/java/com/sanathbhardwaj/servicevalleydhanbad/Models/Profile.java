package com.sanathbhardwaj.servicevalleydhanbad.Models;

public class Profile {

    String shop_name;
    String banner;
    String slogan;


    public Profile(String shop_name, String banner, String slogan) {
        this.shop_name = shop_name;
        this.banner = banner;
        this.slogan = slogan;
    }

    public Profile() {
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}
