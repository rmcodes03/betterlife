package com.example.betterlife.Model;

public class Notice {

    private String notice;
    private String writer;
    private String date;

    public Notice() {
    }

    public Notice(String notice, String writer, String date) {
        this.notice = notice;
        this.writer = writer;
        this.date = date;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
