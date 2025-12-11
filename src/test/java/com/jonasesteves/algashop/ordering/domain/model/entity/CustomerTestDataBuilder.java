package com.jonasesteves.algashop.ordering.domain.model.entity;

import com.jonasesteves.algashop.ordering.domain.model.valueobject.*;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTestDataBuilder {

    private CustomerTestDataBuilder(){}

    public static Customer.BrandNewCustomerBuild brandNewCustomerBuild() {
        return Customer.brandNew()
                .fullName(new FullName("Antony", "Edward Stark"))
                .birthDate(new BirthDate(LocalDate.of(1970, 5, 29)))
                .email(new Email("tony@stark.com"))
                .phone(new Phone("555-111-2222"))
                .document(new Document("0102-03-0405"))
                .promotionNotificationsAllowed(false)
                .address(
                        Address.builder()
                                .number("10880")
                                .street("Malibu Point")
                                .complement("Penthouse")
                                .neighborhood("Malibu Beach")
                                .city("Malibu")
                                .state("California")
                                .zipCode(new ZipCode("90265"))
                                .build()
                );
    }

    public static Customer.ExistingCustomerBuild existingCustomer() {
        return Customer.existing()
                .id(new CustomerId())
                .fullName(new FullName("Antony", "Edward Stark"))
                .birthDate(new BirthDate(LocalDate.of(1970, 5, 29)))
                .email(new Email("tony@stark.com"))
                .phone(new Phone("555-111-2222"))
                .document(new Document("0102-03-0405"))
                .promotionNotificationsAllowed(false)
                .loyaltyPoints(new LoyaltyPoints(10))
                .archived(false)
                .archivedAt(null)
                .registeredAt(OffsetDateTime.now())
                .address(
                        Address.builder()
                                .number("10880")
                                .street("Malibu Point")
                                .complement("Penthouse")
                                .neighborhood("Malibu Beach")
                                .city("Malibu")
                                .state("California")
                                .zipCode(new ZipCode("90265"))
                                .build()
                );
    }

    public static Customer.ExistingCustomerBuild existingAnonimyzedCustomer() {
        return Customer.existing()
                .id(new CustomerId())
                .fullName(new FullName("Anonymous", "Anonymous"))
                .birthDate(null)
                .email(new Email("anonymous@anonymous.com"))
                .phone(new Phone("000-000-0000"))
                .document(new Document("000-00-0000"))
                .promotionNotificationsAllowed(false)
                .archived(true)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(OffsetDateTime.now())
                .loyaltyPoints(new LoyaltyPoints(10))
                .address(Address.builder()
                        .number("Anonymous")
                        .street("Malibu Point")
                        .complement(null)
                        .neighborhood("Malibu Beach")
                        .city("Malibu")
                        .state("California")
                        .zipCode(new ZipCode("90265"))
                        .build()
                );
    }
}
