package com.example.betterlife.Model;

public class Post {

    private String caption;
    private String image;
    private String time;
    private String user;
    private String postId;

    public Post() {
    }

    public Post(String caption, String image, String time, String user, String postId) {
        this.caption = caption;
        this.image = image;
        this.time = time;
        this.user = user;
        this.postId = postId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
