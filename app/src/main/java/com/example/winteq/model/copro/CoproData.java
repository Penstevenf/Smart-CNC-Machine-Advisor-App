package com.example.winteq.model.copro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoproData {
    @Expose
    @SerializedName("copro_number")
    private String copro_number;

    @Expose
    @SerializedName("copro_id")
    private String copro_id;

    @Expose
    @SerializedName("copro_line")
    private String copro_line;

    @Expose
    @SerializedName("copro_station")
    private String copro_station;

    @Expose
    @SerializedName("copro_machine")
    private String copro_machine;

    @Expose
    @SerializedName("corpo_part")
    private String corpo_part;

    @Expose
    @SerializedName("copro_date")
    private String copro_date;

    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("message")
    private String[] message;

    public String getCopro_number() {
        return copro_number;
    }

    public void setCopro_number(String copro_number) {
        this.copro_number = copro_number;
    }

    public String getCopro_id() {
        return copro_id;
    }

    public void setCopro_id(String copro_id) {
        this.copro_id = copro_id;
    }

    public String getCopro_line() {
        return copro_line;
    }

    public void setCopro_line(String copro_line) {
        this.copro_line = copro_line;
    }

    public String getCopro_station() {
        return copro_station;
    }

    public void setCopro_station(String copro_station) {
        this.copro_station = copro_station;
    }

    public String getCopro_machine() {
        return copro_machine;
    }

    public void setCopro_machine(String copro_machine) {
        this.copro_machine = copro_machine;
    }

    public String getCorpo_part() {
        return corpo_part;
    }

    public void setCorpo_part(String corpo_part) {
        this.corpo_part = corpo_part;
    }

    public String getCopro_date() {
        return copro_date;
    }

    public void setCopro_date(String copro_date) {
        this.copro_date = copro_date;
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
