package com.example.winteq.model.help.mekanik;

import java.util.List;

public class HelpResponseDataMec {
    private boolean status;
    private String message;
    private List<HelpDataMec> data;

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

    public List<HelpDataMec> getData() {
        return data;
    }

    public void setData(List<HelpDataMec> data) {
        this.data = data;
    }
}
