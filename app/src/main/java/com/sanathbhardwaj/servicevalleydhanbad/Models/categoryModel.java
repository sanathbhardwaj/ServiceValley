package com.sanathbhardwaj.servicevalleydhanbad.Models;

public class categoryModel {

    String name;
    String image;

    public categoryModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public categoryModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
