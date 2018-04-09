package com.studio.smarters.adminfoodfever;

/**
 * Created by daduc on 09-04-2018.
 */

public class Item {
    String item_name,item_quantity,item_total_price;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_total_price() {
        return item_total_price;
    }

    public void setItem_total_price(String item_total_price) {
        this.item_total_price = item_total_price;
    }

    public Item(String item_name, String item_quantity, String item_total_price) {

        this.item_name = item_name;
        this.item_quantity = item_quantity;
        this.item_total_price = item_total_price;
    }

    public Item() {

    }
}
