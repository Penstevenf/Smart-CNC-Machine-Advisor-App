package com.example.winteq.model.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SensorData {
    @Expose
    @SerializedName("sen_id")
    private String sen_id;

    @Expose
    @SerializedName("sen_line")
    private String sen_line;

    @Expose
    @SerializedName("sen_station")
    private String sen_station;

    @Expose
    @SerializedName("sen_machine")
    private String sen_machine;

    @Expose
    @SerializedName("cut_speed")
    private String cut_speed;

    @Expose
    @SerializedName("rpm")
    private String rpm;

    @Expose
    @SerializedName("feed")
    private String feed;

    @Expose
    @SerializedName("cut_time")
    private String cut_time;

    @Expose
    @SerializedName("error_percentage")
    private String error_percentage;

    @Expose
    @SerializedName("error_cut_speed")
    private String error_cut_speed;

    @Expose
    @SerializedName("error_rpm")
    private String error_rpm;

    @Expose
    @SerializedName("error_feed")
    private String error_feed;

    @Expose
    @SerializedName("error_cut_time")
    private String error_cut_time;

    @Expose
    @SerializedName("sen_status")
    private String sen_status;

    @Expose
    @SerializedName("sen_date")
    private String sen_date;

    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("message")
    private String[] message;

    public String getSen_id() {
        return sen_id;
    }

    public void setSen_id(String sen_id) {
        this.sen_id = sen_id;
    }

    public String getSen_line() {
        return sen_line;
    }

    public void setSen_line(String sen_line) {
        this.sen_line = sen_line;
    }

    public String getSen_station() {
        return sen_station;
    }

    public void setSen_station(String sen_station) {
        this.sen_station = sen_station;
    }

    public String getSen_machine() {
        return sen_machine;
    }

    public void setSen_machine(String sen_machine) {
        this.sen_machine = sen_machine;
    }

    public String getCut_speed() {
        return cut_speed;
    }

    public void setCut_speed(String cut_speed) {
        this.cut_speed = cut_speed;
    }

    public String getRpm() {
        return rpm;
    }

    public void setRpm(String rpm) {
        this.rpm = rpm;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getCut_time() {
        return cut_time;
    }

    public void setCut_time(String cut_time) {
        this.cut_time = cut_time;
    }

    public String getError_percentage() {
        return error_percentage;
    }

    public void setError_percentage(String error_percentage) {
        this.error_percentage = error_percentage;
    }

    public String getError_cut_speed() {
        return error_cut_speed;
    }

    public void setError_cut_speed(String error_cut_speed) {
        this.error_cut_speed = error_cut_speed;
    }

    public String getError_rpm() {
        return error_rpm;
    }

    public void setError_rpm(String error_rpm) {
        this.error_rpm = error_rpm;
    }

    public String getError_feed() {
        return error_feed;
    }

    public void setError_feed(String error_feed) {
        this.error_feed = error_feed;
    }

    public String getError_cut_time() {
        return error_cut_time;
    }

    public void setError_cut_time(String error_cut_time) {
        this.error_cut_time = error_cut_time;
    }

    public String getSen_status() {
        return sen_status;
    }

    public void setSen_status(String sen_status) {
        this.sen_status = sen_status;
    }

    public String getSen_date() {
        return sen_date;
    }

    public void setSen_date(String sen_date) {
        this.sen_date = sen_date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }
}
