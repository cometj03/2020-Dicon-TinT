package com.sunrin.tint;

public class Post_content {
    public String title;
    public String contents;
    private String publisher;

    public Post_content(String title, String contents, String writer){
        this.title = title;
        this.contents = contents;
        this.publisher = writer;
    }

    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title = title;}
    public String getContents() {return this.contents;}
    public void setContents(String contents){ this.contents = contents;}
    public String getPublisher(){return this.publisher;}
    public void setPublisher(String writer){this.publisher = writer;}
}
