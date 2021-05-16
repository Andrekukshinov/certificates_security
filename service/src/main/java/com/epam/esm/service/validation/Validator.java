package com.epam.esm.service.validation;

import com.epam.esm.service.exception.ValidationException;

/**
 * Interface for performing custom validation
 *
 * @param <T> type to be validated
 */
public interface Validator<T> {
    /**
     * Method that performs validation for received object
     *
     * @param object to be validated
     * @throws ValidationException in case of validation errors
     */
    void validate(T object) throws ValidationException;
}
