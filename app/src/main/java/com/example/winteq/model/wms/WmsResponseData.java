package com.example.winteq.model.wms;

import java.util.List;

public class WmsResponseData {
    private boolean status;
    private String message;
    private List<WmsData> data;

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

    public List<WmsData> getData() {
        return data;
    }

    public void setData(List<WmsData> data) {
        this.data = data;
    }

}
