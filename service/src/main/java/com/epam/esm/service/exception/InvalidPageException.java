package com.epam.esm.service.exception;

public class InvalidPageException extends RuntimeException{
    public InvalidPageException() {
        super();
    }

    public InvalidPageException(String message) {
        super(message);
    }

    public InvalidPageException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPageException(Throwable cause) {
        super(cause);
    }
}
