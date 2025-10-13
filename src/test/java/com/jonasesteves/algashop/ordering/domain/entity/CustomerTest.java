package com.jonasesteves.algashop.ordering.domain.entity;

import com.jonasesteves.algashop.ordering.domain.exception.CustomerArchivedException;
import com.jonasesteves.algashop.ordering.domain.valueobject.CustomerId;
import com.jonasesteves.algashop.ordering.domain.valueobject.FullName;
import com.jonasesteves.algashop.ordering.domain.valueobject.LoyaltyPoints;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    void givenInvalidEmail_whenTryCreateCustomer_shouldGenerateException() {

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Customer(
                            new CustomerId(),
                            new FullName("Antony", "Edward Stark"),
                            LocalDate.of(1970, 5, 29),
                            "invalid",
                            "555-111-2222",
                            "0102-03-0405",
                            false,
                            OffsetDateTime.now()
                    );
                });
    }

    @Test
    void givenInvalidEmail_whenTryUpdateCustomerEmail_shouldGenerateException() {

        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Antony", "Edward Stark"),
                LocalDate.of(1970, 5, 29),
                "tony@stark.com",
                "555-111-2222",
                "0102-03-0405",
                false,
                OffsetDateTime.now()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    customer.changeEmail("invalid");
                });
    }

    @Test
    void givenUnarchivedCustomer_whenArchive_shouldAnonymize() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Antony", "Edward Stark"),
                LocalDate.of(1970, 5, 29),
                "tony@stark.com",
                "555-111-2222",
                "0102-03-0405",
                false,
                OffsetDateTime.now()
        );

        customer.archive();

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Anonymous")),
                c -> assertThat(c.email()).isNotEqualTo("tony@stark.com"),
                c -> assertThat(c.phone()).isEqualTo("000-000-0000"),
                c -> assertThat(c.document()).isEqualTo("000-00-0000"),
                c -> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse()
            );
    }

    @Test
    void givenArchivedCustomer_whenTryToUpdate_shouldGenerateException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Anonymous", "Anonymous"),
                null,
                "anonymous@anonymous.com",
                "000-000-0000",
                "000-00-0000",
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                new LoyaltyPoints(10)
        );

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(customer::archive);
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(customer::enablePromotionNotifications);
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(customer::disablePromotionNotifications);
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(() -> customer.changeEmail("any@email.com"));
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(() -> customer.changePhone("555-111-2222"));
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(() -> customer.changeName(new FullName("Anonymous", "Anonymous")));

    }

    @Test
    void givenBrandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Antony", "Edward Stark"),
                LocalDate.of(1970, 5, 29),
                "tony@stark.com",
                "555-111-2222",
                "0102-03-0405",
                false,
                OffsetDateTime.now()
        );

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        Assertions.assertThat(customer.loyaltyPoints().value()).isEqualTo(30);

    }

    @Test
    void givenBrandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerageException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Antony", "Edward Stark"),
                LocalDate.of(1970, 5, 29),
                "tony@stark.com",
                "555-111-2222",
                "0102-03-0405",
                false,
                OffsetDateTime.now()
        );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));

    }
}