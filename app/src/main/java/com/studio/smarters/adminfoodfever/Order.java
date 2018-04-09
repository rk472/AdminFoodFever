package com.studio.smarters.adminfoodfever;

/**
 * Created by daduc on 09-04-2018.
 */

public class Order {
    String name,total_price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public Order(String name, String total_price) {

        this.name = name;
        this.total_price = total_price;
    }

    public Order() {

    }
}
