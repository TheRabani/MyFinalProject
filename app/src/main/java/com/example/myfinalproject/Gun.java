package com.example.myfinalproject;

public class Gun {
    private String modelName; // for ex 19
    private String manufacturer; // for ex Glock
    //    private String imgUrl; // a url to a photo
    private int price; // full price
    private int inStock; // number of them in stock
    //    private int standardMagCapacity; // standard mag capacity
    private String optionsMagCapacity; // options of mag capacity
    private String caliber; // diameter X length
    private int weight; // in gr
//    public int barrelLength; //in mm
//    public int triggerPull; // how much N it takes to pull

    public Gun(String modelName, String manufacturer, /*String imgUrl,*/ int price, int inStock, String optionsMagCapacity, String caliber, int weight) {
        this.modelName = modelName;
        this.manufacturer = manufacturer;
//        this.imgUrl = imgUrl;
        this.price = price;
        this.inStock = inStock;
//        this.standardMagCapacity = standardMagCapacity;
        this.optionsMagCapacity = optionsMagCapacity;
        this.caliber = caliber;
        this.weight = weight;
//        this.barrelLength = barrelLength;
//        this.triggerPull = triggerPull;
    }

    public Gun()
    {

    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

//    public int getStandardMagCapacity() {
//        return standardMagCapacity;
//    }

//    public void setStandardMagCapacity(int standardMagCapacity) {
//        this.standardMagCapacity = standardMagCapacity;
//    }

    public String getOptionsMagCapacity() {
        return optionsMagCapacity;
    }

    public void setOptionsMagCapacity(String optionsMagCapacity) {
        this.optionsMagCapacity = optionsMagCapacity;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

//    public String getImgUrl() {
//        return imgUrl;
//    }
//
//    public void setImgUrl(String imgUrl) {
//        this.imgUrl = imgUrl;
//    }

//    public int getBarrelLength() {
//        return barrelLength;
//    }

//    public void setBarrelLength(int barrelLength) {
//        this.barrelLength = barrelLength;
//    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

//    public int getTriggerPull() {
//        return triggerPull;
//    }

//    public void setTriggerPull(int triggerPull) {
//        this.triggerPull = triggerPull;
//    }

    public String getName() { return modelName; }

    public void setName(String name) {
        this.modelName = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}