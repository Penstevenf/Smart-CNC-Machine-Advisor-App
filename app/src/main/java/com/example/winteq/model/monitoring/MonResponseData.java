package com.example.winteq.model.monitoring;

import java.util.List;

public class MonResponseData {
    private boolean status;
    private String message;
    private List<MonData> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MonData> getData() {
        return data;
    }

    public void setData(List<MonData> data) {
        this.data = data;
    }
}
