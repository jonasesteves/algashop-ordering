package com.jonasesteves.algashop.ordering.domain.model.exception;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.jonasesteves.algashop.ordering.domain.model.exception.ErrorMessages.*;

public class OrderCannotBePlacedException extends DomainException {

//    public OrderCannotBePlacedException(OrderId id) {
//        super(String.format(ORDER_CANNOT_BE_PLACED_WITHOUT_ITEMS_ERROR, id));
//    }


    private OrderCannotBePlacedException(String message) {
        super(message);
    }

    public static OrderCannotBePlacedException noItems(OrderId id) {
        return new OrderCannotBePlacedException(String.format(ORDER_CANNOT_BE_PLACED_WITHOUT_ITEMS_ERROR, id));
    }

    public static OrderCannotBePlacedException noShippingInfo(OrderId id) {
        return new OrderCannotBePlacedException(String.format(ORDER_CANNOT_BE_PLACED_WITHOUT_SHIPPING_INFO_ERROR, id));
    }

    public static OrderCannotBePlacedException noBillingInfo(OrderId id) {
        return new OrderCannotBePlacedException(String.format(ORDER_CANNOT_BE_PLACED_WITHOUT_BILLING_INFO_ERROR, id));
    }

    public static OrderCannotBePlacedException noPaymentMethod(OrderId id) {
        return new OrderCannotBePlacedException(String.format(ORDER_CANNOT_BE_PLACED_WITHOUT_PAYMENT_METHOD_ERROR, id));
    }
}
