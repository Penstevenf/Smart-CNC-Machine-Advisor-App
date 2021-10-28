package com.example.winteq.model.help.elektrik;

import java.util.List;

public class HelpResponseDataElc {
    private boolean status;
    private String message;
    private List<HelpDataElc> data;

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

    public List<HelpDataElc> getData() {
        return data;
    }

    public void setData(List<HelpDataElc> data) {
        this.data = data;
    }
}
