package com.jonasesteves.algashop.ordering.domain.model.customer;

import com.jonasesteves.algashop.ordering.domain.model.DomainService;
import com.jonasesteves.algashop.ordering.domain.model.commons.Address;
import com.jonasesteves.algashop.ordering.domain.model.commons.Document;
import com.jonasesteves.algashop.ordering.domain.model.commons.Email;
import com.jonasesteves.algashop.ordering.domain.model.commons.FullName;
import com.jonasesteves.algashop.ordering.domain.model.commons.Phone;

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
