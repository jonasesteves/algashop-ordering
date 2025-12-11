package com.jonasesteves.algashop.ordering.infrastructure.persistence.entity;

import com.jonasesteves.algashop.ordering.domain.model.utility.IdGenerator;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity.CustomerPersistenceEntityBuilder;

import java.time.LocalDate;

public class CustomerPersistenceEntityTestDataBuilder {

    private CustomerPersistenceEntityTestDataBuilder() {
    }

    public static CustomerPersistenceEntityBuilder someCustomer() {
        return CustomerPersistenceEntity.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .firstName("Ronaldinho")
                .lastName("Gaúcho")
                .birthDate(LocalDate.of(1980, 1, 1))
                .email("ronaldinho@gaucho.com")
                .phone("56998876655")
                .document("456123789")
                .promotionNotificationsAllowed(false)
                .loyaltyPoints(10);
    }
}
