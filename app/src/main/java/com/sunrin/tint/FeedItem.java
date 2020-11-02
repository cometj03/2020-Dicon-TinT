package com.sunrin.tint;

import android.graphics.drawable.Drawable;

public class FeedItem {

    public static Drawable defaultFeedImg;
    public static Drawable defaultUserProfile; // TODO:

    private Drawable[] feed_img;
    private Drawable userProfile;
    private String title, subTitle, timeInterval, userName;

    FeedItem(Drawable[] feed_img, Drawable userProfile, String title, String subTitle, String timeInterval, String userName) {
        setFeed_img(feed_img);
        setUserProfile(userProfile);
        setTitle(title);
        setSubTitle(subTitle);
        setTimeInterval(timeInterval);
        setUserName(userName);
    }

    public Drawable[] getFeed_img() {
        return feed_img;
    }

    public void setFeed_img(Drawable[] feed_img) {
        this.feed_img = feed_img;
    }

    public Drawable getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(Drawable userProfile) {
        this.userProfile = userProfile;
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

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
