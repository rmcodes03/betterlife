package com.example.betterlife.Model;

public class User {

    private String email;
    private String id;
    private String address;
    private String phoneNumber;
    private String name;
    private String profileImageUrl;

    public User(){
    }

    public User(String email, String id, String address, String phoneNumber, String name, String profileImageUrl) {
        this.email = email;
        this.id = id;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
