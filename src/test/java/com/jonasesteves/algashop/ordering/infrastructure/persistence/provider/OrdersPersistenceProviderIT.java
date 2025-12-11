package com.jonasesteves.algashop.ordering.infrastructure.persistence.provider;

import com.jonasesteves.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.entity.Order;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderStatus;
import com.jonasesteves.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        CustomerPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrdersPersistenceProviderIT {
    private final OrdersPersistenceProvider ordersPersistenceProvider;
    private final OrderPersistenceEntityRepository entityRepository;
    private final CustomerPersistenceProvider customerPersistenceProvider;

    @Autowired
    public OrdersPersistenceProviderIT(OrdersPersistenceProvider ordersPersistenceProvider, OrderPersistenceEntityRepository entityRepository, CustomerPersistenceProvider customerPersistenceProvider) {
        this.ordersPersistenceProvider = ordersPersistenceProvider;
        this.entityRepository = entityRepository;
        this.customerPersistenceProvider = customerPersistenceProvider;
    }

    @BeforeEach
    void setup() {
        if (!customerPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customerPersistenceProvider.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        Order order = OrderTestDataBuilder.someOrder().orderStatus(OrderStatus.PLACED).build();
        long orderId = order.id().value().toLong();
        ordersPersistenceProvider.add(order);

        OrderPersistenceEntity persistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PLACED.name());
        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

        order = ordersPersistenceProvider.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        ordersPersistenceProvider.add(order);
        persistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus()).isEqualTo(OrderStatus.PAID.name());
        Assertions.assertThat(persistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldAddAndFindAndNotFailWhenNoTransaction() {
        Order order = OrderTestDataBuilder.someOrder().build();
        ordersPersistenceProvider.add(order);

        Assertions.assertThatNoException().isThrownBy(
                () -> ordersPersistenceProvider.ofId(order.id()).orElseThrow()
        );
    }
}