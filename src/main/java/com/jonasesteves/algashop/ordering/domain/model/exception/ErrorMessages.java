package com.jonasesteves.algashop.ordering.domain.model.exception;

public class ErrorMessages {
    public static final String VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";

    public static final String VALIDATION_ERROR_FULLNAME_IS_NULL = "FullName cannot be null";
    public static final String VALIDATION_ERROR_FULLNAME_IS_BLANK = "FullName cannot be blank";

    public static final String VALIDATION_ERROR_EMAIL_IS_INVALID = "Email is invalid";

    public static final String CUSTOMER_ARCHIVED_ERROR = "Archived customer cannot be changed";

    public static final String CUSTOMER_LOYALTY_POINTS_LESS_THAN_ZERO_ERROR = "Loyalty value cannot be less than zero";

    public static final String ORDER_STATUS_CANNOT_BE_CHANGED_ERROR = "Cannot change order %s status from %s to %s";

    public static final String ORDER_DELIVERY_DATE_CANNOT_BE_PAST_ERROR = "Expected date for order %s cannot be in the past";

    public static final String ORDER_CANNOT_BE_PLACED_WITHOUT_ITEMS_ERROR = "Order %s cannot be placed without items";
    public static final String ORDER_CANNOT_BE_PLACED_WITHOUT_SHIPPING_INFO_ERROR = "Order %s cannot be placed without shipping info";
    public static final String ORDER_CANNOT_BE_PLACED_WITHOUT_BILLING_INFO_ERROR = "Order %s cannot be placed without billing info";
    public static final String ORDER_CANNOT_BE_PLACED_WITHOUT_PAYMENT_METHOD_ERROR = "Order %s cannot be placed without payment method";
    public static final String ORDER_CANNOT_BE_CHANGED_ERROR = "Order %s with status %s cannot be changed";

    public static final String ORDER_DOES_NOT_CONTAIN_ITEM_ERROR = "Order %s does not contain item %s";

    public static final String PRODUCT_OUT_OF_STOCK_ERROR = "Product %s is out of stock";

    public static final String SHOPPING_CART_ITEM_PRODUCT_INCOMPATIBLE_ERROR = "The product %s passed by parameter is different from the current product";

    public static final String SHOPPING_CART_DOES_NOT_CONTAIN_ITEM_ID_ERROR = "The shopping cart %s does not contain item %s";
    public static final String SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT_ID_ERROR = "The shopping cart %s does not contain product %s";
}
