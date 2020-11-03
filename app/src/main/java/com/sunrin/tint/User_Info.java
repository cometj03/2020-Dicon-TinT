package com.sunrin.tint;

public class User_Info {
        public String email;
        public String password;
        private String nickname;

    public User_Info(String email, String password, String nickname){
            this.email = email;
            this.password = password;
            this.nickname = nickname;
        }

        public String getEmail(){return this.email;}
        public void setEmail(String email){this.email = email;}
        public String getPassword() {return this.password;}
        public void setPassword(String password){ this.password = password;}
        public String getNickname(){return this.nickname;}
        public void setNickname(String nickname){this.nickname = nickname;}

}
