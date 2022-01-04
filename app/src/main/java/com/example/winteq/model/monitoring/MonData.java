package com.example.winteq.model.monitoring;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonData {

    @Expose
    @SerializedName("mon_id")
    private String mon_id;

    @Expose
    @SerializedName("mon_no")
    private String mon_no;

    @Expose
    @SerializedName("plant")
    private String plant;

    @Expose
    @SerializedName("line")
    private String line;

    @Expose
    @SerializedName("station")
    private String station;

    @Expose
    @SerializedName("mon_image")
    private String mon_image;

    @Expose
    @SerializedName("mon_status")
    private String mon_status;

    @Expose
    @SerializedName("mon_pic")
    private String mon_pic;

    @Expose
    @SerializedName("mon_desc")
    private String mon_desc;

    @Expose
    @SerializedName("mon_date")
    private String mon_date;

    @Expose
    @SerializedName("parameter1")
    private String parameter1;

    @Expose
    @SerializedName("parameter2")
    private String parameter2;

    @Expose
    @SerializedName("parameter3")
    private String parameter3;

    @Expose
    @SerializedName("parameter4")
    private String parameter4;

    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("message")
    private String[] message;

    public String getMon_id() {
        return mon_id;
    }

    public void setMon_id(String mon_id) {
        this.mon_id = mon_id;
    }

    public String getMon_no() {
        return mon_no;
    }

    public void setMon_no(String mon_no) {
        this.mon_no = mon_no;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getMon_image() {
        return mon_image;
    }

    public void setMon_image(String mon_image) {
        this.mon_image = mon_image;
    }

    public String getMon_status() {
        return mon_status;
    }

    public void setMon_status(String mon_status) {
        this.mon_status = mon_status;
    }

    public String getMon_pic() {
        return mon_pic;
    }

    public void setMon_pic(String mon_pic) {
        this.mon_pic = mon_pic;
    }

    public String getMon_desc() {
        return mon_desc;
    }

    public void setMon_desc(String mon_desc) {
        this.mon_desc = mon_desc;
    }

    public String getMon_date() {
        return mon_date;
    }

    public void setMon_date(String mon_date) {
        this.mon_date = mon_date;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    public String getParameter3() {
        return parameter3;
    }

    public void setParameter3(String parameter3) {
        this.parameter3 = parameter3;
    }

    public String getParameter4() {
        return parameter4;
    }

    public void setParameter4(String parameter4) {
        this.parameter4 = parameter4;
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
