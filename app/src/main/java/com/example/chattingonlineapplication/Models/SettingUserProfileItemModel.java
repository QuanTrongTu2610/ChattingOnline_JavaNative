package com.example.chattingonlineapplication.Models;

public class SettingUserProfileItemModel {
    private int itemIcon;
    private String itemName;

    public SettingUserProfileItemModel(int itemIcon, String itemName) {
        this.itemIcon = itemIcon;
        this.itemName = itemName;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
