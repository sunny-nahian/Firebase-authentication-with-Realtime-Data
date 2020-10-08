package com.sunny.firebaseauthrealtimedata;

public class userDataModel {

    private String first;
    private String last;
    private String email;
    private String phone;
    private String password;
    private String conpassword;

    public userDataModel(){}  //Defalte Constractor

    public userDataModel(String first, String last, String email, String phone, String password, String conpassword) {
        this.first = first;
        this.last = last;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.conpassword = conpassword;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConpassword() {
        return conpassword;
    }

    public void setConpassword(String conpassword) {
        this.conpassword = conpassword;
    }
}
