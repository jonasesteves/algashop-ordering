package com.jonasesteves.algashop.ordering.domain.model.exception;

import com.jonasesteves.algashop.ordering.domain.model.entity.OrderStatus;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.jonasesteves.algashop.ordering.domain.model.exception.ErrorMessages.ORDER_CANNOT_BE_CHANGED_ERROR;

public class OrderCannotBeChangedException extends DomainException {
    public OrderCannotBeChangedException(OrderId id, OrderStatus status) {
        super(String.format(ORDER_CANNOT_BE_CHANGED_ERROR, id, status));
    }
}
