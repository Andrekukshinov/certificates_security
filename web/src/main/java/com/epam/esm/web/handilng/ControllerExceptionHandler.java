package com.epam.esm.web.handilng;

import com.epam.esm.persistence.exception.SortingException;
import com.epam.esm.service.exception.DeleteTagInUseException;
import com.epam.esm.service.exception.EntityAlreadyExistsException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.InvalidPageException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.web.exception.InvalidValueException;
import com.epam.esm.web.model.BindExceptionModel;
import com.epam.esm.web.model.ExceptionModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(ControllerExceptionHandler.class);
    private static final int INTERNAL_SERVER_ERROR = 500_01;
    private static final int ENTITY_NOT_FOUND_EXCEPTION = 404_01;
    private static final int ENTITY_EXISTS_EXCEPTION = 409_01;
    private static final int NOT_FOUND_EXCEPTION = 404_00;
    private static final int BAD_REQUEST = 400_00;
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    private static final String INVALID_VALUE = "invalid value: ";

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        List<FieldError> allErrors = ex.getFieldErrors();
        List<String> messages = allErrors.stream().map(error -> INVALID_VALUE + error.getField()).collect(Collectors.toList());
        BindExceptionModel model = new BindExceptionModel(messages, BAD_REQUEST);
        return super.handleExceptionInternal(ex, model, headers, status, request);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object> handleConflict(EntityNotFoundException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(ex.getMessage(), ENTITY_NOT_FOUND_EXCEPTION);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = EntityAlreadyExistsException.class)
    protected ResponseEntity<Object> handleConflict(EntityAlreadyExistsException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(ex.getMessage(), ENTITY_EXISTS_EXCEPTION);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        String first = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("validation failed");
        ExceptionModel body = new ExceptionModel(first, BAD_REQUEST);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel("invalid value " + ex.getValue(), BAD_REQUEST);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = ValidationException.class)
    protected ResponseEntity<Object> handleConflict(ValidationException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(ex.getMessage(), BAD_REQUEST);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = DeleteTagInUseException.class)
    protected ResponseEntity<Object> handleConflict(DeleteTagInUseException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(ex.getMessage(), ENTITY_EXISTS_EXCEPTION);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = InvalidValueException.class)
    protected ResponseEntity<Object> handleConflict(InvalidValueException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(ex.getMessage(), BAD_REQUEST);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = SortingException.class)
    protected ResponseEntity<Object> handleConflict(SortingException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(ex.getMessage(), BAD_REQUEST);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = InvalidPageException.class)
    protected ResponseEntity<Object> handleConflict(InvalidPageException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(ex.getMessage(), BAD_REQUEST);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        String message = ex.getCause().getMessage();
        System.out.println(message);
        ExceptionModel body = new ExceptionModel(ex.getCause().getMessage(), BAD_REQUEST);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(ex.getMessage(), BAD_REQUEST);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(ex.getMessage(), NOT_FOUND_EXCEPTION);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        ExceptionModel body = new ExceptionModel(INTERNAL_SERVER_ERROR_MESSAGE, INTERNAL_SERVER_ERROR);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}

