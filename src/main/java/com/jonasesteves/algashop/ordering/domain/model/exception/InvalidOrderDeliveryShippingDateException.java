package com.jonasesteves.algashop.ordering.domain.model.exception;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.jonasesteves.algashop.ordering.domain.model.exception.ErrorMessages.ORDER_DELIVERY_DATE_CANNOT_BE_PAST_ERROR;

public class InvalidOrderDeliveryShippingDateException extends DomainException {
    public InvalidOrderDeliveryShippingDateException(OrderId id) {
        super(String.format(ORDER_DELIVERY_DATE_CANNOT_BE_PAST_ERROR, id));
    }
}
