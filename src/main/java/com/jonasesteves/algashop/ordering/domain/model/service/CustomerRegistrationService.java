package com.jonasesteves.algashop.ordering.domain.model.service;

import com.jonasesteves.algashop.ordering.domain.model.entity.Customer;
import com.jonasesteves.algashop.ordering.domain.model.exception.CustomerEmailIsInUseException;
import com.jonasesteves.algashop.ordering.domain.model.repository.Customers;
import com.jonasesteves.algashop.ordering.domain.model.utility.DomainService;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Address;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.BirthDate;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Document;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Email;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.FullName;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Phone;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;

@DomainService
public class CustomerRegistrationService {

    private final Customers customers;

    public CustomerRegistrationService(Customers customers) {
        this.customers = customers;
    }

    public Customer register(FullName fullName, BirthDate birthDate, Email email, Phone phone, Document document, Boolean promotionNotificationsAllowed, Address address) {
        /* Este serviço deve ser responsável por instanciar um customer,
        por isso recebe os parâmetros e não a instância. (M2 9.4 1:15) */
        Customer customer = Customer.brandNew()
                .fullName(fullName)
                .birthDate(birthDate)
                .email(email)
                .phone(phone)
                .document(document)
                .promotionNotificationsAllowed(promotionNotificationsAllowed)
                .address(address)
                .build();

        verifyEmailUniqueness(customer.email(), customer.id());
        return customer;
    }

    public void changeEmail(Customer customer, Email newEmail) {
        verifyEmailUniqueness(newEmail, customer.id());
        customer.changeEmail(newEmail);
    }

    private void verifyEmailUniqueness(Email email, CustomerId customerId) {
        if (!customers.isEmailUnique(email, customerId)) {
            throw new CustomerEmailIsInUseException();
        }
    }
}
