package com.jonasesteves.algashop.ordering.infrastructure.persistence.repository;

import com.jonasesteves.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfig.class)
class CustomerPersistenceEntityRepositoryIT {

    private final CustomerPersistenceEntityRepository repository;

    @Autowired
    public CustomerPersistenceEntityRepositoryIT(CustomerPersistenceEntityRepository repository) {
        this.repository = repository;
    }

    @Test
    void shouldPersist() {
        CustomerPersistenceEntity customerPersistenceEntity = CustomerPersistenceEntityTestDataBuilder.someCustomer().build();

        repository.saveAndFlush(customerPersistenceEntity);

        Assertions.assertThat(repository.existsById(customerPersistenceEntity.getId())).isTrue();
    }

    @Test
    void shouldCount() {
        long customersCount = repository.count();
        Assertions.assertThat(customersCount).isZero();
    }

    @Test
    void shouldSetAuditingValue() {
        CustomerPersistenceEntity customerPersistenceEntity = CustomerPersistenceEntityTestDataBuilder.someCustomer().build();

        customerPersistenceEntity = repository.saveAndFlush(customerPersistenceEntity);

        Assertions.assertWith(customerPersistenceEntity,
                c -> Assertions.assertThat(c.getCreatedByUserId()).isNotNull(),
                c -> Assertions.assertThat(c.getLastModifiedByUserId()).isNotNull(),
                c -> Assertions.assertThat(c.getLastModifiedAt()).isNotNull()
        );
    }
}
