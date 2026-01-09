package com.jonasesteves.algashop.ordering.domain.model.commons;

import com.jonasesteves.algashop.ordering.domain.model.FieldValidations;

import static com.jonasesteves.algashop.ordering.domain.model.ErrorMessages.VALIDATION_ERROR_EMAIL_IS_INVALID;

public record Email(String value) {

    public Email {
        FieldValidations.requiresValidEmail(value, VALIDATION_ERROR_EMAIL_IS_INVALID);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
