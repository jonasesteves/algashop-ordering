package com.jonasesteves.algashop.ordering.application.service;

import com.jonasesteves.algashop.ordering.application.model.AddressData;
import com.jonasesteves.algashop.ordering.application.model.CustomerInput;
import com.jonasesteves.algashop.ordering.domain.model.commons.Address;
import com.jonasesteves.algashop.ordering.domain.model.commons.Document;
import com.jonasesteves.algashop.ordering.domain.model.commons.Email;
import com.jonasesteves.algashop.ordering.domain.model.commons.FullName;
import com.jonasesteves.algashop.ordering.domain.model.commons.Phone;
import com.jonasesteves.algashop.ordering.domain.model.commons.ZipCode;
import com.jonasesteves.algashop.ordering.domain.model.customer.BirthDate;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerRegistrationService;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class CustomerManagementApplicationService {
    private final CustomerRegistrationService customerRegistrationService;
    private final Customers customers;

    public CustomerManagementApplicationService(CustomerRegistrationService customerRegistrationService, Customers customers) {
        this.customerRegistrationService = customerRegistrationService;
        this.customers = customers;
    }

    @Transactional
    public UUID create(CustomerInput input) {
        Objects.requireNonNull(input);

        AddressData address = input.getAddress();
        Customer customer = customerRegistrationService.register(
                new FullName(input.getFirstName(), input.getLastName()),
                new BirthDate(input.getBirthDate()),
                new Email(input.getEmail()),
                new Phone(input.getPhone()),
                new Document(input.getDocument()),
                input.getPromotionNotificationsAllowed(),
                Address.builder()
                        .number(address.getNumber())
                        .street(address.getStreet())
                        .complement(address.getComplement())
                        .neighborhood(address.getNeighborhood())
                        .city(address.getCity())
                        .state(address.getState())
                        .zipCode(new ZipCode(address.getZipCode()))
                        .build()
        );

        customers.add(customer);

        return customer.id().value();
    }
}
