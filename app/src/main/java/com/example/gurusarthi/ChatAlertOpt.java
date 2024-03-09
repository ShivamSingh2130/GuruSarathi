package com.example.gurusarthi;

import android.graphics.drawable.Drawable;

public class ChatAlertOpt {

    private String mTitle;
    private int mIcon;
    private boolean isAdded;


    public  ChatAlertOpt(String title, int icon, boolean misAdded) {
        mTitle = title;
        mIcon = icon;
        misAdded = isAdded;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public boolean isAddable() {
        return isAdded;
    }

    public void setAddable(boolean addable) {
        isAdded = addable;
    }

}