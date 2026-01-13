package com.jonasesteves.algashop.ordering.application.customer.management;

import com.jonasesteves.algashop.ordering.application.commons.AddressData;
import com.jonasesteves.algashop.ordering.application.utility.Mapper;
import com.jonasesteves.algashop.ordering.domain.model.commons.Address;
import com.jonasesteves.algashop.ordering.domain.model.commons.Document;
import com.jonasesteves.algashop.ordering.domain.model.commons.Email;
import com.jonasesteves.algashop.ordering.domain.model.commons.FullName;
import com.jonasesteves.algashop.ordering.domain.model.commons.Phone;
import com.jonasesteves.algashop.ordering.domain.model.commons.ZipCode;
import com.jonasesteves.algashop.ordering.domain.model.customer.BirthDate;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerNotFoundException;
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
    private final Mapper mapper;

    public CustomerManagementApplicationService(CustomerRegistrationService customerRegistrationService, Customers customers, Mapper mapper) {
        this.customerRegistrationService = customerRegistrationService;
        this.customers = customers;
        this.mapper = mapper;
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

    public CustomerOutput findById(UUID customerId) {
        Objects.requireNonNull(customerId);
        Customer customer = customers.ofId(new CustomerId(customerId)).orElseThrow(CustomerNotFoundException::new);
        return mapper.convert(customer, CustomerOutput.class);
    }

    @Transactional
    public void update(UUID customerId, CustomerUpdateInput input) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(input);

        Customer customer = customers.ofId(new CustomerId(customerId)).orElseThrow(CustomerNotFoundException::new);

        customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
        customer.changePhone(new Phone(input.getPhone()));

        if (Boolean.TRUE.equals(input.getPromotionNotificationsAllowed())) {
            customer.enablePromotionNotifications();
        } else {
            customer.disablePromotionNotifications();
        }

        AddressData address = input.getAddress();
        customer.changeAddress(Address.builder()
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
    }

    @Transactional
    public void archive(UUID customerId) {
        Objects.requireNonNull(customerId);
        Customer customer = customers.ofId(new CustomerId(customerId)).orElseThrow(CustomerNotFoundException::new);
        customer.archive();
        customers.add(customer);
    }

    @Transactional
    public void changeEmail(UUID customerId, String newEmail) {
        Objects.requireNonNull(customerId);
        Customer customer = customers.ofId(new CustomerId(customerId)).orElseThrow(CustomerNotFoundException::new);
        Email email = new Email(newEmail);
        customerRegistrationService.changeEmail(customer, email);
        customers.add(customer);
    }
}
