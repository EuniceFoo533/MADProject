package com.example.fragment;

public class Model {

    private String task_id;
    private String task_name;
    private String task_description;
    private String date;
    private String time;
    private String priority_level;
    private String prior_color;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPriority_level() {
        return priority_level;
    }

    public void setPriority_level(String priority_level) {
        this.priority_level = priority_level;
    }

    public String getPrior_color() {
        return prior_color;
    }

    public void setPrior_color(String prior_color) {
        this.prior_color = prior_color;
    }


    public Model(String task_id, String task_name, String task_description, String date, String time, String priority_level, String prior_color) {
        this.task_id = task_id;
        this.task_name = task_name;
        this.task_description = task_description;
        this.date = date;
        this.time = time;
        this.priority_level = priority_level;
        this.prior_color = prior_color;
    }


}
