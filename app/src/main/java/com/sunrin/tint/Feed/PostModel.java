package com.sunrin.tint.Feed;

import java.util.List;

public class PostModel {

    public enum Filter {
        eMakeUp, eHair, eFashion, eNail, eDiet
    }

    private String userName, userEmail;
    private String title, subTitle, content, date;
    private List<String> imgUris;
    private List<Filter> filters;
}
