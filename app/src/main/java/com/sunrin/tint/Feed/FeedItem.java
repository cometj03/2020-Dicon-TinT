package com.sunrin.tint.Feed;

import android.net.Uri;

import com.sunrin.tint.Model.PostModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Intent 로 객체를 보내기 위해 Serializable 상속
public class FeedItem implements Serializable {

    private List<PostModel.Filter> filters;
    private List<Uri> images;
    private List<String> imgIDs;
    private String title, subTitle, timeInterval, content, userName, userEmail;

    public FeedItem(List<PostModel.Filter> filters, List<String> imgIDs, String title, String subTitle, String content, String timeInterval, String userName, String userEmail) {
        this.filters = filters;
        this.imgIDs = imgIDs;
        this.images = new ArrayList<>();
        this.title = title;
        this.subTitle = subTitle;
        this.timeInterval = timeInterval;
        this.content = content;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public void addImage(Uri uri) {
        this.images.add(uri);
    }

    public List<PostModel.Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<PostModel.Filter> filters) {
        this.filters = filters;
    }

    public List<Uri> getImages() {
        return images;
    }

    public void setImages(List<Uri> images) {
        this.images = images;
    }

    public List<String> getImgIDs() {
        return imgIDs;
    }

    public void setImgIDs(List<String> imgIDs) {
        this.imgIDs = imgIDs;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
