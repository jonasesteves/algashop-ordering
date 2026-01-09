package com.jonasesteves.algashop.ordering.domain.model.customer;

import com.jonasesteves.algashop.ordering.domain.model.commons.Address;
import com.jonasesteves.algashop.ordering.domain.model.commons.Email;
import com.jonasesteves.algashop.ordering.domain.model.commons.FullName;
import com.jonasesteves.algashop.ordering.domain.model.commons.Phone;
import com.jonasesteves.algashop.ordering.domain.model.commons.ZipCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    void givenInvalidEmail_whenTryCreateCustomer_shouldGenerateException() {

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    CustomerTestDataBuilder.brandNewCustomerBuild().email(new Email("invalid")).build();
                });
    }

    @Test
    void givenInvalidEmail_whenTryUpdateCustomerEmail_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    customer.changeEmail(new Email("invalid"));
                });
    }

    @Test
    void givenUnarchivedCustomer_whenArchive_shouldAnonymize() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        customer.archive();

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Anonymous")),
                c -> assertThat(c.email().value()).isNotEqualTo("tony@stark.com"),
                c -> assertThat(c.phone().value()).isEqualTo("000-000-0000"),
                c -> assertThat(c.document().value()).isEqualTo("000-00-0000"),
                c -> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
                c -> assertThat(c.address()).isEqualTo(
                        Address.builder()
                                .number("Anonymous")
                                .street("Malibu Point")
                                .complement(null)
                                .neighborhood("Malibu Beach")
                                .city("Malibu")
                                .state("California")
                                .zipCode(new ZipCode("90265"))
                                .build()
                )
        );
    }

    @Test
    void givenArchivedCustomer_whenTryToUpdate_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.existingAnonimyzedCustomer().build();

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(customer::archive);
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(customer::enablePromotionNotifications);
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(customer::disablePromotionNotifications);
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(() -> customer.changeEmail(new Email("any@email.com")));
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(() -> customer.changePhone(new Phone("555-111-2222")));
        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(() -> customer.changeName(new FullName("Anonymous", "Anonymous")));

    }

    @Test
    void givenBrandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        Assertions.assertThat(customer.loyaltyPoints().value()).isEqualTo(30);

    }

    @Test
    void givenBrandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerageException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuild().build();

        Assertions.assertThatNoException().isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));

    }
}