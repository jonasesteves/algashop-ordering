package com.jonasesteves.algashop.ordering.domain.model.customer;

import com.jonasesteves.algashop.ordering.domain.model.DomainException;

import static com.jonasesteves.algashop.ordering.domain.model.ErrorMessages.CUSTOMER_ARCHIVED_ERROR;

public class CustomerArchivedException extends DomainException {

    public CustomerArchivedException() {
        super(CUSTOMER_ARCHIVED_ERROR);
    }

    public CustomerArchivedException(Throwable cause) {
        super(CUSTOMER_ARCHIVED_ERROR, cause);
    }
}
