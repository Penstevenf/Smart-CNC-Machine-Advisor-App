package com.example.winteq.model.asset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssetData {
    @Expose
    @SerializedName("asset_id")
    private String asset_id;

    @Expose
    @SerializedName("asset_no")
    private String asset_no;

    @Expose
    @SerializedName("asset_name")
    private String asset_name;

    @Expose
    @SerializedName("asset_line")
    private String asset_line;

    @Expose
    @SerializedName("asset_station")
    private String asset_station;

    @Expose
    @SerializedName("machine_name")
    private String machine_name;

    @Expose
    @SerializedName("asset_part")
    private String asset_part;

    @Expose
    @SerializedName("asset_category")
    private String asset_category;

    @Expose
    @SerializedName("asset_qty")
    private String asset_qty;

    @Expose
    @SerializedName("asset_unit")
    private String asset_unit;

    @Expose
    @SerializedName("lifetime")
    private String lifetime;

    @Expose
    @SerializedName("asset_status")
    private String asset_status;

    @Expose
    @SerializedName("date_replace")
    private String date_replace;

    @Expose
    @SerializedName("date_register")
    private String date_register;

    @Expose
    @SerializedName("date_notif")
    private String date_notif;

    @Expose
    @SerializedName("date_update")
    private String date_update;

    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("message")
    private String[] message;

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getAsset_no() {
        return asset_no;
    }

    public void setAsset_no(String asset_no) {
        this.asset_no = asset_no;
    }

    public String getAsset_name() {
        return asset_name;
    }

    public void setAsset_name(String asset_name) {
        this.asset_name = asset_name;
    }

    public String getAsset_line() {
        return asset_line;
    }

    public void setAsset_line(String asset_line) {
        this.asset_line = asset_line;
    }

    public String getAsset_station() {
        return asset_station;
    }

    public void setAsset_station(String asset_station) {
        this.asset_station = asset_station;
    }

    public String getMachine_name() {
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }

    public String getAsset_part() {
        return asset_part;
    }

    public void setAsset_part(String asset_part) {
        this.asset_part = asset_part;
    }

    public String getAsset_category() {
        return asset_category;
    }

    public void setAsset_category(String asset_category) {
        this.asset_category = asset_category;
    }

    public String getAsset_qty() {
        return asset_qty;
    }

    public void setAsset_qty(String asset_qty) {
        this.asset_qty = asset_qty;
    }

    public String getAsset_unit() {
        return asset_unit;
    }

    public void setAsset_unit(String asset_unit) {
        this.asset_unit = asset_unit;
    }

    public String getAsset_status() {
        return asset_status;
    }

    public void setAsset_status(String asset_status) {
        this.asset_status = asset_status;
    }

    public String getLifetime() {
        return lifetime;
    }

    public void setLifetime(String lifetime) {
        this.lifetime = lifetime;
    }

    public String getDate_replace() {
        return date_replace;
    }

    public void setDate_replace(String date_replace) {
        this.date_replace = date_replace;
    }

    public String getDate_register() {
        return date_register;
    }

    public void setDate_register(String date_register) {
        this.date_register = date_register;
    }

    public String getDate_notif() {
        return date_notif;
    }

    public void setDate_notif(String date_notif) {
        this.date_notif = date_notif;
    }

    public String getDate_update() {
        return date_update;
    }

    public void setDate_update(String date_update) {
        this.date_update = date_update;
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
