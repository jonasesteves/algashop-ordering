package com.jonasesteves.algashop.ordering.domain.exception;

import com.jonasesteves.algashop.ordering.domain.entity.OrderStatus;
import com.jonasesteves.algashop.ordering.domain.valueobject.id.OrderId;

import static com.jonasesteves.algashop.ordering.domain.exception.ErrorMessages.ORDER_STATUS_CANNOT_BE_CHANGED_ERROR;

public class OrderStatusCannotBeChangedException extends DomainException {

    public OrderStatusCannotBeChangedException(String message) {
        super(message);
    }

    public OrderStatusCannotBeChangedException(OrderId id, OrderStatus status, OrderStatus newStatus) {
        super(String.format(ORDER_STATUS_CANNOT_BE_CHANGED_ERROR, id, status, newStatus));
    }
}
