package com.epam.esm.service.exception;

public class DeletedRoleException extends RuntimeException{
    public DeletedRoleException() {
        super();
    }

    public DeletedRoleException(String message) {
        super(message);
    }

    public DeletedRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedRoleException(Throwable cause) {
        super(cause);
    }
}
