package com.sunrin.tint.Feed;

import com.sunrin.tint.Util.TimeAgo;

import java.io.Serializable;

// Intent 로 객체를 보내기 위해 Serializable 상속
// 피드 아이템을 최신순으로 정렬하기 위해 Comparable 상속
public class FeedItem implements Serializable, Comparable<FeedItem> {

    //private Filter filter;
    private String ImageID;
    private String title, subTitle, dateFormat, userName, content;

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

    @Override
    public int compareTo(FeedItem feedItem) {
        long targetTime = TimeAgo.getTime(feedItem.getDateFormat());
        long thisTime = TimeAgo.getTime(this.dateFormat);

        if (thisTime > targetTime)
            return 1;
        else if (thisTime < targetTime)
            return -1;
        return 0;
    }

//
//    public Filter getFilter() {
//        return filter;D
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
