package com.example.winteq.model.help.mekanik;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpDataMec {
    @Expose
    @SerializedName("help_mec_id")
    private String help_mec_id;

    @Expose
    @SerializedName("mec_date")
    private String mec_date;

    @Expose
    @SerializedName("item_mec")
    private String item_mec;

    @Expose
    @SerializedName("desc_mec")
    private String desc_mec;

    @Expose
    @SerializedName("pdf_mec_image")
    private String pdf_mec_image;

    @Expose
    @SerializedName("pdf_mec")
    private String pdf_mec;

    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("message")
    private String[] message;

    public String getHelp_mec_id() {
        return help_mec_id;
    }

    public void setHelp_mec_id(String help_mec_id) {
        this.help_mec_id = help_mec_id;
    }

    public String getMec_date() {
        return mec_date;
    }

    public void setMec_date(String mec_date) {
        this.mec_date = mec_date;
    }

    public String getItem_mec() {
        return item_mec;
    }

    public void setItem_mec(String item_mec) {
        this.item_mec = item_mec;
    }

    public String getDesc_mec() {
        return desc_mec;
    }

    public void setDesc_mec(String desc_mec) {
        this.desc_mec = desc_mec;
    }

    public String getPdf_mec_image() {
        return pdf_mec_image;
    }

    public void setPdf_mec_image(String pdf_mec_image) {
        this.pdf_mec_image = pdf_mec_image;
    }

    public String getPdf_mec() {
        return pdf_mec;
    }

    public void setPdf_mec(String pdf_mec) {
        this.pdf_mec = pdf_mec;
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
