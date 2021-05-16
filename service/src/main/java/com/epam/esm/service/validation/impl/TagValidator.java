package com.epam.esm.service.validation.impl;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class TagValidator implements Validator<Tag> {

    private static final int MAX_NAME_LENGTH = 20;
    private static final int MIN_ID_VALUE = 1;

    @Override
    public void validate(Tag object) throws ValidationException {
        validateName(object.getName());
        validateId(object.getId());
    }

    private void validateId(Long id) throws ValidationException {
        if (id != null && (id < MIN_ID_VALUE)) {
            throw new ValidationException("id cannot be less than 1!");
        }
    }

    private void validateName(String name) throws ValidationException {
        if (name != null && (name.length() > MAX_NAME_LENGTH)) {
            throw new ValidationException("name cannot be longer than 20 symbols!");
        } else if (name != null && name.isEmpty()) {
            throw new ValidationException("name cannot be empty!");
        }
    }
}
