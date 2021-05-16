package com.epam.esm.web.model;

public class ExceptionModel {
    private String message;
    private int statusCode;

    public ExceptionModel(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public ExceptionModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "ExceptionModel{" +
                "message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExceptionModel that = (ExceptionModel) o;

        if (getStatusCode() != that.getStatusCode()) {
            return false;
        }
        return getMessage() != null ? getMessage().equals(that.getMessage()) : that.getMessage() == null;
    }

    @Override
    public int hashCode() {
        int result = getMessage() != null ? getMessage().hashCode() : 0;
        result = 31 * result + getStatusCode();
        return result;
    }
}
