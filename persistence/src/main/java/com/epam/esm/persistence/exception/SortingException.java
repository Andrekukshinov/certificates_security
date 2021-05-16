package com.epam.esm.persistence.exception;

public class SortingException extends RuntimeException {
    public SortingException() {
        super();
    }

    public SortingException(String message) {
        super(message);
    }

    public SortingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SortingException(Throwable cause) {
        super(cause);
    }
}
