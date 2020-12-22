package com.sunrin.tint.Model;

import java.util.ArrayList;
import java.util.List;

public class UserModel {

    private String name, email;
    private List<String> postID, storageID;

    public UserModel() {}

    public UserModel(String name, String email) {
        this.name = name;
        this.email = email;
        postID = new ArrayList<>();
        storageID = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getPostID() {
        return postID;
    }

    public void setPostID(List<String> postID) {
        this.postID = postID;
    }

    public List<String> getStorageID() {
        return storageID;
    }

    public void setStorageID(List<String> storageID) {
        this.storageID = storageID;
    }
}
