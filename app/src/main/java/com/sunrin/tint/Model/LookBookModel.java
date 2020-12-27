package com.sunrin.tint.Model;

import com.sunrin.tint.Filter;
import com.sunrin.tint.Util.DateUtil;

import java.io.Serializable;
import java.util.List;

public class LookBookModel {

    private List<String> linkList;
    private String id, mainImage;
    private String userName, userEmail, date;

    public LookBookModel() {}

    public LookBookModel(List<String> listList, String mainImage) {
        this.linkList = listList;
        this.mainImage = mainImage;
    }
}
