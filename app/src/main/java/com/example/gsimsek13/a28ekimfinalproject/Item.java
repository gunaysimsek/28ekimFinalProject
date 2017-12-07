package com.example.gsimsek13.a28ekimfinalproject;

/**
 * Created by Efehan on 7.12.2017.
 */

public class Item {

    String itemName;
    int itemImage;

    public Item(String itemName,int itemImage)
    {
        this.itemImage=itemImage;
        this.itemName=itemName;
    }
    public String getItemName()
    {
        return itemName;
    }
    public int getItemImage()
    {
        return itemImage;
    }
}