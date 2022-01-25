package com.example.winteq.model.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryData {
    @Expose
    @SerializedName("id_hist")
    private String id_hist;

    @Expose
    @SerializedName("line_hist")
    private String line_hist;

    @Expose
    @SerializedName("station_hist")
    private String station_hist;

    @Expose
    @SerializedName("machine_hist")
    private String machine_hist;

    @Expose
    @SerializedName("pic_hist")
    private String pic_hist;

    @Expose
    @SerializedName("date_problem")
    private String date_problem;

    @Expose
    @SerializedName("date_solution")
    private String date_solution;

    @Expose
    @SerializedName("status_hist")
    private String status_hist;

    @Expose
    @SerializedName("image_problem")
    private String image_problem;

    @Expose
    @SerializedName("image_solution")
    private String image_solution;

    @Expose
    @SerializedName("problem")
    private String problem;

    @Expose
    @SerializedName("solution")
    private String solution;

    @Expose
    @SerializedName("part_problem")
    private String part_problem;

    @Expose
    @SerializedName("part_solution")
    private String part_solution;

    @Expose
    @SerializedName("qty_problem")
    private String qty_problem;

    @Expose
    @SerializedName("qty_solution")
    private String qty_solution;

    @Expose
    @SerializedName("type_hist")
    private String type_hist;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("title_solution")
    private String title_solution;

    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("message")
    private String[] message;

    public String getId_hist() {
        return id_hist;
    }

    public void setId_hist(String id_hist) {
        this.id_hist = id_hist;
    }

    public String getLine_hist() {
        return line_hist;
    }

    public void setLine_hist(String line_hist) {
        this.line_hist = line_hist;
    }

    public String getStation_hist() {
        return station_hist;
    }

    public void setStation_hist(String station_hist) {
        this.station_hist = station_hist;
    }

    public String getMachine_hist() {
        return machine_hist;
    }

    public void setMachine_hist(String machine_hist) {
        this.machine_hist = machine_hist;
    }

    public String getPic_hist() {
        return pic_hist;
    }

    public void setPic_hist(String pic_hist) {
        this.pic_hist = pic_hist;
    }

    public String getDate_problem() {
        return date_problem;
    }

    public void setDate_problem(String date_problem) {
        this.date_problem = date_problem;
    }

    public String getDate_solution() {
        return date_solution;
    }

    public void setDate_solution(String date_solution) {
        this.date_solution = date_solution;
    }

    public String getStatus_hist() {
        return status_hist;
    }

    public void setStatus_hist(String status_hist) {
        this.status_hist = status_hist;
    }

    public String getImage_problem() {
        return image_problem;
    }

    public void setImage_problem(String image_problem) {
        this.image_problem = image_problem;
    }

    public String getImage_solution() {
        return image_solution;
    }

    public void setImage_solution(String image_solution) {
        this.image_solution = image_solution;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getPart_problem() {
        return part_problem;
    }

    public void setPart_problem(String part_problem) {
        this.part_problem = part_problem;
    }

    public String getPart_solution() {
        return part_solution;
    }

    public void setPart_solution(String part_solution) {
        this.part_solution = part_solution;
    }

    public String getQty_problem() {
        return qty_problem;
    }

    public void setQty_problem(String qty_problem) {
        this.qty_problem = qty_problem;
    }

    public String getQty_solution() {
        return qty_solution;
    }

    public void setQty_solution(String qty_solution) {
        this.qty_solution = qty_solution;
    }

    public String getType_hist() {
        return type_hist;
    }

    public void setType_hist(String type_hist) {
        this.type_hist = type_hist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
