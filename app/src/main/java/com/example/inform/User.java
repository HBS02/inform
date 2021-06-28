package com.example.inform;

public class User {
    private String email;
    private String Uid;
    private String name;
    private String address;
    public String chatName;
    public boolean chat;

    public void setChatName(String chatName) {this.chatName = chatName;}
    public User() { }

    public User(String Uid, String email, String name, String address) {
        this.Uid = Uid;
        this.email = email;
        this.name = name;
        this.address = address;
        chatName = "";
        chat = false;
    }
    public String getAddress() {
        return address;
    }
    public String getUid() {
        return Uid;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUid(String uid) {
        Uid = uid;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}

