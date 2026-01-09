package com.jonasesteves.algashop.ordering.domain.model.customer;

import com.jonasesteves.algashop.ordering.domain.model.commons.Address;
import com.jonasesteves.algashop.ordering.domain.model.commons.Document;
import com.jonasesteves.algashop.ordering.domain.model.commons.Email;
import com.jonasesteves.algashop.ordering.domain.model.commons.FullName;
import com.jonasesteves.algashop.ordering.domain.model.commons.Phone;
import com.jonasesteves.algashop.ordering.domain.model.commons.ZipCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class CustomerRegistrationServiceTest {

    @Mock
    private Customers customers;

    @InjectMocks
    private CustomerRegistrationService customerRegistrationService;

    @Test
    void shouldRegister() {
        Mockito.when(customers.isEmailUnique(Mockito.any(Email.class), Mockito.any(CustomerId.class))).thenReturn(true);

        Customer customer = customerRegistrationService.register(
                new FullName("Jonas", "Esteves"),
                new BirthDate(LocalDate.of(2000, 1, 1)),
                new Email("jonas@email.com"),
                new Phone("999-111-222"),
                new Document("215-632-0153"),
                true,
                Address.builder()
                        .number("2121")
                        .street("Name Street")
                        .neighborhood("Someville Neib")
                        .city("Lisbon")
                        .state("Lisbon")
                        .zipCode(new ZipCode("40998"))
                        .complement("Ap. 223")
                        .build()
        );

        Assertions.assertThat(customer.fullName()).isEqualTo(new FullName("Jonas", "Esteves"));
        Assertions.assertThat(customer.email()).isEqualTo(new Email("jonas@email.com"));
    }
}