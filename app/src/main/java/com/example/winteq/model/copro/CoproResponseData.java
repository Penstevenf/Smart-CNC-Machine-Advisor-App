package com.example.winteq.model.copro;

import java.util.List;

public class CoproResponseData {
    private boolean status;
    private String message;
    private List<CoproData> data;

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

    public List<CoproData> getData() {
        return data;
    }

    public void setData(List<CoproData> data) {
        this.data = data;
    }

}
