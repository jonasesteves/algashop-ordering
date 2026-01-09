package com.jonasesteves.algashop.ordering.application.service.customer.management;

import com.jonasesteves.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.jonasesteves.algashop.ordering.application.commons.AddressData;
import com.jonasesteves.algashop.ordering.application.customer.management.CustomerInput;
import com.jonasesteves.algashop.ordering.application.customer.management.CustomerOutput;
import com.jonasesteves.algashop.ordering.application.customer.management.CustomerUpdateInput;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @Test
    void shouldRegister() {
        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput).extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getEmail,
                CustomerOutput::getBirthDate
        ).containsExactly(
                customerId,
                "Ronaldinho",
                "Gaúcho",
                "ronaldinho@gaucho.com",
                LocalDate.of(1980, 1,1)
        );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    void shouldUpdate() {
        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        CustomerUpdateInput customerUpdateInput = CustomerUpdateInputTestDataBuilder.someCustomerUpdate().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId, customerUpdateInput);

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput).extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getEmail,
                CustomerOutput::getBirthDate
        ).containsExactly(
                customerId,
                "Ronaldo",
                "Fenômeno",
                "ronaldinho@gaucho.com",
                LocalDate.of(1980, 1,1)
        );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }
}