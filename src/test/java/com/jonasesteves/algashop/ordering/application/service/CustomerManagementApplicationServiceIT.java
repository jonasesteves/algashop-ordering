package com.jonasesteves.algashop.ordering.application.service;

import com.jonasesteves.algashop.ordering.application.model.AddressData;
import com.jonasesteves.algashop.ordering.application.model.CustomerInput;
import com.jonasesteves.algashop.ordering.domain.model.commons.ZipCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @Test
    void shouldRegister() {
        CustomerInput input = CustomerInput.builder()
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
                )
                .build();
        UUID customerId = customerManagementApplicationService.create(input);

        Assertions.assertThat(customerId).isNotNull();
    }
}