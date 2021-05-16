package com.epam.esm.service.exception;

public class DeleteTagInUseException extends RuntimeException {
    public DeleteTagInUseException() {
    }

    public DeleteTagInUseException(String message) {
        super(message);
    }

    public DeleteTagInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteTagInUseException(Throwable cause) {
        super(cause);
    }
}
