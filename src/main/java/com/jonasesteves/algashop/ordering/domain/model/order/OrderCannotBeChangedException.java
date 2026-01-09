package com.jonasesteves.algashop.ordering.domain.model.order;

import com.jonasesteves.algashop.ordering.domain.model.DomainException;

import static com.jonasesteves.algashop.ordering.domain.model.ErrorMessages.ORDER_CANNOT_BE_CHANGED_ERROR;

public class OrderCannotBeChangedException extends DomainException {
    public OrderCannotBeChangedException(OrderId id, OrderStatus status) {
        super(String.format(ORDER_CANNOT_BE_CHANGED_ERROR, id, status));
    }
}
