package com.jonasesteves.algashop.ordering.domain.model.exception;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderItemId;

import static com.jonasesteves.algashop.ordering.domain.model.exception.ErrorMessages.ORDER_DOES_NOT_CONTAIN_ITEM_ERROR;

public class OrderDoesNotContainItemException extends DomainException {

    public OrderDoesNotContainItemException(OrderId id, OrderItemId orderItemId) {
        super(String.format(ORDER_DOES_NOT_CONTAIN_ITEM_ERROR, id, orderItemId));
    }
}
