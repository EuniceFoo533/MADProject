package com.example.fragment;

public class Feedback {
    String email;
    String recommend;
    String suggestion;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public Feedback(String email, String recommend, String suggestion) {
        this.email = email;
        this.recommend = recommend;
        this.suggestion = suggestion;
    }


}
