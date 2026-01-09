package com.jonasesteves.algashop.ordering.domain.model.order;

import com.jonasesteves.algashop.ordering.domain.model.DomainException;

import static com.jonasesteves.algashop.ordering.domain.model.ErrorMessages.ORDER_STATUS_CANNOT_BE_CHANGED_ERROR;

public class OrderStatusCannotBeChangedException extends DomainException {

    public OrderStatusCannotBeChangedException(String message) {
        super(message);
    }

    public OrderStatusCannotBeChangedException(OrderId id, OrderStatus status, OrderStatus newStatus) {
        super(String.format(ORDER_STATUS_CANNOT_BE_CHANGED_ERROR, id, status, newStatus));
    }
}
