package com.example.gurusarthi;

import android.graphics.drawable.Drawable;

public class ChatAlertOpt {

    private String mTitle;
    private int mIcon;
    private boolean mCanAdd;
    private boolean mCanRemove;


    public  ChatAlertOpt(String title, int icon, boolean canAdd, boolean canRemove) {
        mTitle = title;
        mIcon = icon;
        mCanAdd = canAdd;
        mCanRemove = canRemove;
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
        return mCanAdd;
    }

    public void setAddable(boolean addable) {
        mCanAdd = addable;
    }

    public boolean isRemovable() {
        return mCanRemove;
    }

    public void setRemovable(boolean removable) {
        mCanRemove = removable;
    }
}