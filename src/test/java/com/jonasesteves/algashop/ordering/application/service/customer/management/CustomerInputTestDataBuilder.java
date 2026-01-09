package com.jonasesteves.algashop.ordering.application.service.customer.management;

import com.jonasesteves.algashop.ordering.application.commons.AddressData;
import com.jonasesteves.algashop.ordering.application.customer.management.CustomerInput;

import java.time.LocalDate;

public class CustomerInputTestDataBuilder {

    public static CustomerInput.CustomerInputBuilder someCustomer() {
        return CustomerInput.builder()
                .firstName("Ronaldinho")
                .lastName("Gaúcho")
                .birthDate(LocalDate.of(1980, 1, 1))
                .email("ronaldinho@gaucho.com")
                .phone("585.855.2588")
                .document("221-22-2223")
                .promotionNotificationsAllowed(false)
                .address(
                        AddressData.builder()
                                .number("10880")
                                .street("Malibu Point")
                                .complement("Penthouse")
                                .neighborhood("Malibu Beach")
                                .city("Malibu")
                                .state("California")
                                .zipCode("90265")
                                .build()
                );
    }
}
