package com.jonasesteves.algashop.ordering.domain.exception;

import com.jonasesteves.algashop.ordering.domain.valueobject.id.OrderId;

import static com.jonasesteves.algashop.ordering.domain.exception.ErrorMessages.ORDER_DELIVERY_DATE_CANNOT_BE_PAST_ERROR;

public class InvalidOrderDeliveryShippingDateException extends DomainException {
    public InvalidOrderDeliveryShippingDateException(OrderId id) {
        super(String.format(ORDER_DELIVERY_DATE_CANNOT_BE_PAST_ERROR, id));
    }
}
