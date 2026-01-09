package com.jonasesteves.algashop.ordering.infrastructure.persistence.customer;

import com.jonasesteves.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class CustomerPersistenceEntityTestDataBuilder {

    private CustomerPersistenceEntityTestDataBuilder() {
    }

    public static CustomerPersistenceEntity.CustomerPersistenceEntityBuilder someCustomer() {
        return CustomerPersistenceEntity.builder()
                .id(DEFAULT_CUSTOMER_ID.value())
                .firstName("Ronaldinho")
                .lastName("Gaúcho")
                .birthDate(LocalDate.of(1980, 1, 1))
                .email("ronaldinho@gaucho.com")
                .phone("56998876655")
                .document("456123789")
                .promotionNotificationsAllowed(false)
                .loyaltyPoints(10)
                .registeredAt(OffsetDateTime.now())
                .archived(false)
                .archivedAt(null)
                .address(AddressEmbeddable.builder()
                        .number("123")
                        .state("Twenty Street")
                        .complement("A")
                        .neighborhood("Village Neighborhood")
                        .city("SimCity")
                        .state("SimState")
                        .zipCode("34434")
                        .build()
                );
    }
}
