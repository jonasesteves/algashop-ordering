package com.jonasesteves.algashop.ordering.application.service.customer.management;

import com.jonasesteves.algashop.ordering.application.commons.AddressData;
import com.jonasesteves.algashop.ordering.application.customer.management.CustomerUpdateInput;

public class CustomerUpdateInputTestDataBuilder {

    public static CustomerUpdateInput.CustomerUpdateInputBuilder someCustomerUpdate() {
        return CustomerUpdateInput.builder()
                .firstName("Ronaldo")
                .lastName("Fenômeno")
                .phone("123-123-4567")
                .promotionNotificationsAllowed(true)
                .address(AddressData.builder()
                        .street("Modified Street")
                        .number("1111")
                        .complement("Complement Mod")
                        .neighborhood("Modif Neib")
                        .city("Modif City")
                        .state("New State")
                        .zipCode("21345")
                        .build());
    }
}
