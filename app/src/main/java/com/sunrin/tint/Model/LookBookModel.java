package com.sunrin.tint.Model;

import com.sunrin.tint.Filter;
import com.sunrin.tint.Util.DateUtil;

import java.io.Serializable;
import java.util.List;

public class LookBookModel implements Comparable<LookBookModel>, Serializable {

    private List<Filter> filter;
    private List<String> image;
    private String id;
    private String userName, userEmail;
    private String contents, date;

    public LookBookModel(){};

    public LookBookModel(List<Filter> filters, List<String> image, String content){
        this.filter = filters;
        this.image = image;
        this.contents = content;
        this.date = DateUtil.getDateFormat();
        this.userName = "username";
        this.userEmail = "email@email.com";
    }

    @Override
    public int compareTo(LookBookModel lookBookModel) {
        long targetTime = DateUtil.getTime(lookBookModel.getDate());
        long thisTime = DateUtil.getTime(this.getDate());

        if(thisTime > targetTime)
            return 1;
        else if(thisTime<targetTime)
            return -1;
        return 0;
    }

    public String getId(){return id;}

    public void setId(String id) {
        this.id = id;
    }

    public List<Filter> getFilters() {
        return filter;
    }

    public void setFilters(List<Filter> filter) {
        this.filter = filter;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
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

    public String getContents() {
        return contents;
    }

    public void setContents(String content) {
        this.contents = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
