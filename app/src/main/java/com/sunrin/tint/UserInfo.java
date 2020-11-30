package com.sunrin.tint;

import java.util.ArrayList;

public class UserInfo {

    private String userName, userID;
    private ArrayList<String> postID, storageID;

    public UserInfo() {}

    public UserInfo(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
        postID = new ArrayList<>();
        storageID = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
