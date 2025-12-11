package com.jonasesteves.algashop.ordering.domain.model.repository;

import com.jonasesteves.algashop.ordering.domain.model.entity.Customer;
import com.jonasesteves.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Email;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Phone;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.provider.CustomerPersistenceProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

@DataJpaTest
@Import({
        CustomerPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class
})
class CustomersIT {

    private final Customers customers;

    @Autowired
    CustomersIT(Customers customers) {
        this.customers = customers;
    }

    @Test
    void shouldPersistAndFind() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        CustomerId customerId = customer.id();

        customers.add(customer);
        Optional<Customer> possibleCustomer = customers.ofId(customerId);

        Assertions.assertThat(possibleCustomer).isPresent();

        Customer savedCustomer = possibleCustomer.get();

        Assertions.assertThat(savedCustomer).satisfies(
                c -> Assertions.assertThat(c.id()).isEqualTo(customerId),
                c -> Assertions.assertThat(c.fullName()).isEqualTo(customer.fullName()),
                c -> Assertions.assertThat(c.birthDate()).isEqualTo(customer.birthDate()),
                c -> Assertions.assertThat(c.phone()).isEqualTo(customer.phone()),
                c -> Assertions.assertThat(c.document()).isEqualTo(customer.document()),
                c -> Assertions.assertThat(c.isPromotionNotificationsAllowed()).isEqualTo(customer.isPromotionNotificationsAllowed()),
                c -> Assertions.assertThat(c.loyaltyPoints()).isEqualTo(customer.loyaltyPoints()),
                c -> Assertions.assertThat(c.archivedAt()).isEqualTo(customer.archivedAt()),
                c -> Assertions.assertThat(c.archivedAt()).isEqualTo(customer.archivedAt()),
                c -> Assertions.assertThat(c.registeredAt()).isEqualTo(customer.registeredAt()),
                c -> Assertions.assertThat(c.address()).isEqualTo(customer.address())
        );
    }

    @Test
    void shouldUpdateExistingCustomer() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);
        customer = customers.ofId(customer.id()).orElseThrow();

        String newPhone = "90090990";
        customer.changePhone(new Phone(newPhone));
        customers.add(customer);
        customer = customers.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(customer.phone().value()).isEqualTo(newPhone);
    }

    @Test
    void shouldNotAllowStaleUpdates() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        String customerPhone = customer.phone().value();
        customers.add(customer);

        Customer customer1 = customers.ofId(customer.id()).orElseThrow();
        Customer customer2 = customers.ofId(customer.id()).orElseThrow();

        String email = "some@email.com";
        customer1.changeEmail(new Email(email));
        customers.add(customer1);

        customer2.changePhone(new Phone("09099090"));

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class).isThrownBy(
                () -> customers.add(customer2)
        );

        Customer savedCustomer = customers.ofId(customer.id()).orElseThrow();

        Assertions.assertThat(savedCustomer.email().value()).isEqualTo(email);
        Assertions.assertThat(savedCustomer.phone().value()).isEqualTo(customerPhone);
    }

    @Test
    void shouldCountExistingCustomers() {
        Assertions.assertThat(customers.count()).isZero();

        Customer customer1 = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer1);

        Customer customer2 = CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).build();
        customers.add(customer2);

        Assertions.assertThat(customers.count()).isEqualTo(2L);
    }

    @Test
    void shouldReturnIfCustomerExists() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        Assertions.assertThat(customers.exists(customer.id())).isTrue();
        Assertions.assertThat(customers.exists(new CustomerId())).isFalse();
    }
}
