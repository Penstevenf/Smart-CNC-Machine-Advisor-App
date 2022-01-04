package com.example.winteq.model.asset;

import java.util.List;

public class AssetResponseData {
    private boolean status;
    private String message;
    private List<AssetData> data;

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

    public List<AssetData> getData() {
        return data;
    }

    public void setData(List<AssetData> data) {
        this.data = data;
    }

}
