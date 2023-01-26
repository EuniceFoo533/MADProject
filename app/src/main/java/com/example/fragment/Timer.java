package com.example.fragment;

public class Timer {
    public String getStart_id() {
        return start_id;
    }

    public void setStart_id(String start_id) {
        this.start_id = start_id;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Timer(String start_id, String current_date, String start_time, String userID) {
        this.start_id = start_id;
        this.current_date = current_date;
        this.start_time = start_time;
        this.userID = userID;
    }

    String start_id ,current_date, start_time,userID;







}
