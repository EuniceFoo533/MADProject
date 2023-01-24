package com.example.fragment;

public class Timer {
    String start_id ,current_date, start_time;

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

    public Timer(String start_id, String current_date, String start_time) {
        this.start_id = start_id;
        this.current_date = current_date;
        this.start_time = start_time;
    }






}
