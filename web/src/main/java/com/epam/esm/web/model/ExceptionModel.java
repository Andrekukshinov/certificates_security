package com.epam.esm.web.model;

public class ExceptionModel {
    private String message;
    private int status;

    public ExceptionModel(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ExceptionModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ExceptionModel{" +
                "message='" + message + '\'' +
                ", statusCode=" + status +
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

        if (getStatus() != that.getStatus()) {
            return false;
        }
        return getMessage() != null ? getMessage().equals(that.getMessage()) : that.getMessage() == null;
    }

    @Override
    public int hashCode() {
        int result = getMessage() != null ? getMessage().hashCode() : 0;
        result = 31 * result + getStatus();
        return result;
    }
}
