package com.epam.esm.service.validation.impl;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TagValidatorTest {


    private static Stream<Arguments> dataProvider() {
        String tooLongName = "123456789012345678901234567890123456789012345678901";
        Tag longName = new Tag(1L, tooLongName);
        Tag invalidId = new Tag(-1L, "tooLongName");
        Tag emptyName = new Tag(1L, "");
        return Stream.of(
                Arguments.of(longName, "name cannot be longer than 50 symbols!"),
                Arguments.of(invalidId, "id cannot be less than 1!"),
                Arguments.of(emptyName, "name cannot be empty!")
        );
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    void testValidateShouldThrowValidationExceptionWhenInvalid(Tag tagDto, String msg) {
        assertThrows(ValidationException.class, () -> new TagValidator().validate(tagDto), msg);
    }
}
