package com.example.link.models;

public class MessageModel {

    String mId, message;
    Long timeStamp;

    public MessageModel(String mId, String message, Long timeStamp) {
        this.mId = mId;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public MessageModel(String mId, String message) {
        this.mId = mId;
        this.message = message;
    }

    public MessageModel() {
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
