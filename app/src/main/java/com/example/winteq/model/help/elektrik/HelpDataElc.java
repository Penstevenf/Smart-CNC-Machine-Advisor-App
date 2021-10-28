package com.example.winteq.model.help.elektrik;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpDataElc {
    @Expose
    @SerializedName("help_elc_id")
    private String help_elc_id;

    @Expose
    @SerializedName("elc_date")
    private String elc_date;

    @Expose
    @SerializedName("item_elc")
    private String item_elc;

    @Expose
    @SerializedName("pdf_elc_image")
    private String pdf_elc_image;

    @Expose
    @SerializedName("pdf_elc")
    private String pdf_elc;

    @Expose
    @SerializedName("encodedPDF")
    private String encodedPDF;

    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("message")
    private String[] message;

    public String getHelp_elc_id() {
        return help_elc_id;
    }

    public void setHelp_elc_id(String help_elc_id) {
        this.help_elc_id = help_elc_id;
    }

    public String getElc_date() {
        return elc_date;
    }

    public void setElc_date(String elc_date) {
        this.elc_date = elc_date;
    }

    public String getItem_elc() {
        return item_elc;
    }

    public void setItem_elc(String item_elc) {
        this.item_elc = item_elc;
    }

    public String getPdf_elc_image() {
        return pdf_elc_image;
    }

    public void setPdf_elc_image(String pdf_elc_image) {
        this.pdf_elc_image = pdf_elc_image;
    }

    public String getPdf_elc() {
        return pdf_elc;
    }

    public void setPdf_elc(String pdf_elc) {
        this.pdf_elc = pdf_elc;
    }

    public String getEncodedPDF() {
        return encodedPDF;
    }

    public void setEncodedPDF(String encodedPDF) {
        this.encodedPDF = encodedPDF;
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
