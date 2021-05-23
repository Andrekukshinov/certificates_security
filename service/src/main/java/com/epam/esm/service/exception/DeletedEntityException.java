package com.epam.esm.service.exception;

public class DeletedEntityException extends RuntimeException{
    public DeletedEntityException() {
        super();
    }

    public DeletedEntityException(String message) {
        super(message);
    }

    public DeletedEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedEntityException(Throwable cause) {
        super(cause);
    }
}
