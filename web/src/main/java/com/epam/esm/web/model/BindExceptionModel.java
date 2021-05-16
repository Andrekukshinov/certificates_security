package com.epam.esm.web.model;

import java.util.List;

public class BindExceptionModel {
    private List<String> messages;
    private int statusCode;

    public BindExceptionModel() {
    }

    public BindExceptionModel(List<String> messages, int statusCode) {
        this.messages = messages;
        this.statusCode = statusCode;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
