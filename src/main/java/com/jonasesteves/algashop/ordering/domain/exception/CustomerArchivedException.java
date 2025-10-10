package com.jonasesteves.algashop.ordering.domain.exception;

import static com.jonasesteves.algashop.ordering.domain.exception.ErrorMessages.CUSTOMER_ARCHIVED_ERROR;

public class CustomerArchivedException extends DomainException {

    public CustomerArchivedException() {
        super(CUSTOMER_ARCHIVED_ERROR);
    }

    public CustomerArchivedException(Throwable cause) {
        super(CUSTOMER_ARCHIVED_ERROR, cause);
    }
}
