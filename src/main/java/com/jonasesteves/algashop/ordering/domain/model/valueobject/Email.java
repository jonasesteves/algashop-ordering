package com.jonasesteves.algashop.ordering.domain.model.valueobject;

import com.jonasesteves.algashop.ordering.domain.model.validator.FieldValidations;

import static com.jonasesteves.algashop.ordering.domain.model.exception.ErrorMessages.VALIDATION_ERROR_EMAIL_IS_INVALID;

public record Email(String value) {

    public Email {
        FieldValidations.requiresValidEmail(value, VALIDATION_ERROR_EMAIL_IS_INVALID);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
