package com.sunrin.tint;

import java.util.ArrayList;

public class UserInfo {

    private String userName, userEmail;
    private ArrayList<String> postID, storageID;

    public UserInfo() {}

    public UserInfo(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
        postID = new ArrayList<>();
        storageID = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public ArrayList<String> getPostID() {
        return postID;
    }

    public void setPostID(ArrayList<String> postID) {
        this.postID = postID;
    }

    public ArrayList<String> getStorageID() {
        return storageID;
    }

    public void setStorageID(ArrayList<String> storageID) {
        this.storageID = storageID;
    }
}
