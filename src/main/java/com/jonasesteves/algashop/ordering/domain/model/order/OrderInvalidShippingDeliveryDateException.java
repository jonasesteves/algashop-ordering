package com.jonasesteves.algashop.ordering.domain.model.order;

import com.jonasesteves.algashop.ordering.domain.model.DomainException;

import static com.jonasesteves.algashop.ordering.domain.model.ErrorMessages.ORDER_DELIVERY_DATE_CANNOT_BE_PAST_ERROR;

public class OrderInvalidShippingDeliveryDateException extends DomainException {
    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ORDER_DELIVERY_DATE_CANNOT_BE_PAST_ERROR, id));
    }
}
