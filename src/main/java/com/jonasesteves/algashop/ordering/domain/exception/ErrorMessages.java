package com.jonasesteves.algashop.ordering.domain.exception;

public class ErrorMessages {
    public static final String VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";

    public static final String VALIDATION_ERROR_FULLNAME_IS_NULL = "FullName cannot be null";
    public static final String VALIDATION_ERROR_FULLNAME_IS_BLANK = "FullName cannot be blank";

    public static final String VALIDATION_ERROR_EMAIL_IS_INVALID = "Email is invalid";

    public static final String CUSTOMER_ARCHIVED_ERROR = "Archived customer cannot be changed";

    public static final String CUSTOMER_LOYALTY_POINTS_LESS_THAN_ZERO_ERROR = "Loyalty value cannot be less than zero";
}
