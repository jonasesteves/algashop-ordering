package com.jonasesteves.algashop.ordering.domain.exception;

import static com.jonasesteves.algashop.ordering.domain.exception.ErrorMessages.CUSTOMER_LOYALTY_POINTS_LESS_THAN_ZERO_ERROR;

public class LoyaltyPointsException extends DomainException {

    public LoyaltyPointsException() {
        super(CUSTOMER_LOYALTY_POINTS_LESS_THAN_ZERO_ERROR);
    }

    public LoyaltyPointsException(Throwable cause) {
        super(CUSTOMER_LOYALTY_POINTS_LESS_THAN_ZERO_ERROR, cause);
    }
}
