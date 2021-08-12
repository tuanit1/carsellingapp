package com.example.autosellingapp.items;

public class ChatItem {
    private String sender_uid;
    private String receiver_uid;
    private String type;
    private String message;
    private String isseen;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getReceiver_uid() {
        return receiver_uid;
    }

    public void setReceiver_uid(String receiver_uid) {
        this.receiver_uid = receiver_uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsseen() {
        return isseen;
    }

    public void setIsseen(String isseen) {
        this.isseen = isseen;
    }

    public ChatItem(){

    }

    public ChatItem(String sender_uid, String receiver_uid, String type, String message, String isseen) {
        this.sender_uid = sender_uid;
        this.receiver_uid = receiver_uid;
        this.type = type;
        this.message = message;
        this.isseen = isseen;
    }
}
