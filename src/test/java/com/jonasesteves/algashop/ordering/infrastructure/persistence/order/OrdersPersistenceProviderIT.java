package com.jonasesteves.algashop.ordering.infrastructure.persistence.order;

import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.order.Order;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderStatus;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderTestDataBuilder;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
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
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrdersPersistenceProviderIT {
    private final OrdersPersistenceProvider ordersPersistenceProvider;
    private final OrderPersistenceEntityRepository entityRepository;
    private final CustomersPersistenceProvider customersPersistenceProvider;

    @Autowired
    public OrdersPersistenceProviderIT(OrdersPersistenceProvider ordersPersistenceProvider, OrderPersistenceEntityRepository entityRepository, CustomersPersistenceProvider customersPersistenceProvider) {
        this.ordersPersistenceProvider = ordersPersistenceProvider;
        this.entityRepository = entityRepository;
        this.customersPersistenceProvider = customersPersistenceProvider;
    }

    @BeforeEach
    void setup() {
        if (!customersPersistenceProvider.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customersPersistenceProvider.add(CustomerTestDataBuilder.existingCustomer().build());
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