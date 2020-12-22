package com.sunrin.tint.Model;

import com.sunrin.tint.Util.TimeUtil;

import java.util.List;

public class PostModel {

    // string to enum : Enum.valueOf("eMakeUp");
    public enum Filter {
        eMakeUp, eHair, eFashion, eNail, eDiet
    }

    private List<Filter> filters;
    private List<String> imgIDs;
    private String userName, userEmail;
    private String title, subTitle, content, date;

    public PostModel() {}

    public PostModel(List<Filter> filters, List<String> imgIDs, String title, String subTitle, String content) {
        this.filters = filters;
        this.imgIDs = imgIDs;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.date = TimeUtil.getDateFormat();
        this.userName = "username";
        this.userEmail = "email@email.com";
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<String> getImgIDs() {
        return imgIDs;
    }

    public void setImgIDs(List<String> imgIDs) {
        this.imgIDs = imgIDs;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
