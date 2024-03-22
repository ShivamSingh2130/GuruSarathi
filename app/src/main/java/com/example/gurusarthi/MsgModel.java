package com.example.gurusarthi;

import android.net.Uri;

public class MsgModel {


    String imageUri;
    String message;
    String senderid;
    long timeStamp;
    public MsgModel() {
        // Default constructor required for Firebase
    }
    public MsgModel(String message, String senderid, long timeStamp, String imageUri) {
        this.message = message;
        this.senderid = senderid;
        this.timeStamp = timeStamp;
        this.imageUri = imageUri;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
