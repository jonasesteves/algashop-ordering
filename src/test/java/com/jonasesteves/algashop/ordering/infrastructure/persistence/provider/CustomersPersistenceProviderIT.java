package com.jonasesteves.algashop.ordering.infrastructure.persistence.provider;

import com.jonasesteves.algashop.ordering.domain.model.entity.Customer;
import com.jonasesteves.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Email;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@DataJpaTest
@Import({
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class CustomersPersistenceProviderIT {

    private final CustomersPersistenceProvider customersPersistenceProvider;
    private final CustomerPersistenceEntityRepository repository;

    @Autowired
    CustomersPersistenceProviderIT(CustomersPersistenceProvider customersPersistenceProvider, CustomerPersistenceEntityRepository repository) {
        this.customersPersistenceProvider = customersPersistenceProvider;
        this.repository = repository;
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();
        UUID customerId = customer.id().value();

        customersPersistenceProvider.add(customer);
        CustomerPersistenceEntity customerPersistenceEntity = repository.findById(customerId).orElseThrow();

        Assertions.assertThat(customerPersistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(customerPersistenceEntity.getLastModifiedByUserId()).isNotNull();
        Assertions.assertThat(customerPersistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(customerPersistenceEntity.getVersion()).isZero();

        customer = customersPersistenceProvider.ofId(customer.id()).orElseThrow();
        String someEmail = "other@email.com";
        customer.changeEmail(new Email(someEmail));
        customersPersistenceProvider.add(customer);

        customerPersistenceEntity = repository.findById(customerId).orElseThrow();

        Assertions.assertThat(customerPersistenceEntity.getEmail()).isEqualTo(someEmail);
        Assertions.assertThat(customerPersistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(customerPersistenceEntity.getLastModifiedByUserId()).isNotNull();
        Assertions.assertThat(customerPersistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(customerPersistenceEntity.getVersion()).isEqualTo(1);
    }

    @Test
    void shouldAddAndFindAndNotFailWhenNoTransaction() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customersPersistenceProvider.add(customer);

        Assertions.assertThatNoException().isThrownBy(
                () -> customersPersistenceProvider.ofId(customer.id()).orElseThrow()
        );
    }
}