package com.sunrin.tint.Feed;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

// Intent 로 객체를 보내기 위해 Serializable 사용
public class FeedItem implements Serializable {

    //private Filter filter;
//    private Drawable feed_img;
//    private Drawable userProfile;
    private String ImageID;
    private String title, subTitle, dateFormat, userName, content;

//    FeedItem(FeedItem.Filter filter, Drawable feed_img, Drawable userProfile, String title, String subTitle, String dateFormat, String userName, String content) {
//        setFilter(filter);
//        setFeed_img(feed_img);
//        setUserProfile(userProfile);
//        setTitle(title);
//        setSubTitle(subTitle);
//        setDateFormat(dateFormat);
//        setUserName(userName);
//        setContent(content);
//    }

    public FeedItem() {}

    public FeedItem(String ImageID, String title, String subTitle, String dateFormat, String userName, String content) {
        //this.filter = filter;
        this.ImageID = ImageID;
        this.title = title;
        this.subTitle = subTitle;
        this.dateFormat = dateFormat;
        this.userName = userName;
        this.content = content;
    }

    public enum Filter {
        eMakeUp, eHair, eFashion, eNail, eDiet
    }
//
//    public Filter getFilter() {
//        return filter;
//    }
//
//    public void setFilter(Filter filter) {
//        this.filter = filter;
//    }

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
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

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
