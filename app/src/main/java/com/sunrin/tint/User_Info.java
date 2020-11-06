package com.sunrin.tint;

public class User_Info {

    private String nickname;

    public User_Info(String nickname){
        this.nickname = nickname;
    }

    public String getNickname(){ return this.nickname; }
    public void setNickname(String nickname){ this.nickname = nickname; }

}
