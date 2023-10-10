package com.example.betterlife.Model;

public class News {
    private String headline;
    private String publisher;
    private String date;
    private String imageUrl;

    public News(String headline, String publisher, String date, String imageUrl) {
        this.headline = headline;
        this.publisher = publisher;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
