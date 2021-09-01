package com.example.autosellingapp.items;

import java.io.Serializable;

public class UserFirebase implements Serializable {
    private String uid;
    private String status;

    public UserFirebase(){

    }

    public UserFirebase(String uid, String status) {
        this.uid = uid;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
