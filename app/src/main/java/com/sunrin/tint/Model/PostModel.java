package com.sunrin.tint.Model;

import android.net.Uri;

import com.sunrin.tint.Filter;
import com.sunrin.tint.Util.DateUtil;

import java.io.Serializable;
import java.util.List;

// Intent 로 객체를 보내기 위해 Serializable 상속
// 최신순으로 정렬하기 위해 Comparable 상속
public class PostModel implements Comparable<PostModel>, Serializable {
    // Model for Firebase Firestore

    private List<Filter> filters;
    private transient List<Uri> images; // transient : Except Serialize
    private String id;
    private String userName, userEmail;
    private String title, subTitle, content, date;

    public PostModel() {}

    public PostModel(String id, List<Filter> filters, List<Uri> images, String title, String subTitle, String content) {
        this.id = id;
        this.filters = filters;
        this.images = images;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.date = DateUtil.getDateFormat();
        this.userName = "username";
        this.userEmail = "email@email.com";
    }

    @Override
    public int compareTo(PostModel o) {
        long targetTime = DateUtil.getTime(o.getDate());
        long thisTime = DateUtil.getTime(this.getDate());

        if (thisTime > targetTime)
            return 1;
        else if (thisTime < targetTime)
            return -1;
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Uri> getImages() {
        return images;
    }

    public void setImages(List<Uri> images) {
        this.images = images;
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
