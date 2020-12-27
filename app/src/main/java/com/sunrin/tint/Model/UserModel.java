package com.sunrin.tint.Model;

import com.sunrin.tint.Filter;

import java.util.ArrayList;
import java.util.List;

public class UserModel {

    private String name, email, status, profile;
    private List<String> postID, storageID, lookBookID;
    private List<Filter> userFilters;

    public UserModel() {}

    public UserModel(String email) {
        this.email = email;
        this.name = "";
        this.status = "";
        this.profile = null;
        this.postID = new ArrayList<>();
        this.storageID = new ArrayList<>();
        this.lookBookID = new ArrayList<>();
        this.userFilters = new ArrayList<>();
    }

    public UserModel(String name, String email, String status, String profile) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.profile = profile;
        this.postID = new ArrayList<>();
        this.storageID = new ArrayList<>();
        this.lookBookID = new ArrayList<>();
        this.userFilters = new ArrayList<>();
    }

    // add IDs
    public void addPostID(String id) {
        this.postID.add(id);
    }

    public void addStorageID(String id) {
        this.storageID.add(id);
    }

    // delete IDs
    public void deletePostID(String id) {
        this.postID.remove(id);
    }

    public String getName() {
        return name;
    }

    // *** get set *** //
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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

    public List<Filter> getUserFilters() {
        return userFilters;
    }

    public void setUserFilters(List<Filter> userFilters) {
        this.userFilters = userFilters;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getLookBookID() {
        return lookBookID;
    }

    public void setLookBookID(List<String> lookBookID) {
        this.lookBookID = lookBookID;
    }
}
