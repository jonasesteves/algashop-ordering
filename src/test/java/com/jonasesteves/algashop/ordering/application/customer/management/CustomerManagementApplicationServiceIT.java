package com.jonasesteves.algashop.ordering.application.customer.management;

import com.jonasesteves.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.jonasesteves.algashop.ordering.application.customer.query.CustomerOutput;
import com.jonasesteves.algashop.ordering.application.customer.query.CustomerQueryService;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerArchivedException;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerEmailIsInUseException;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.jonasesteves.algashop.ordering.infrastructure.listener.customer.CustomerEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @Autowired
    @MockitoSpyBean
    private CustomerNotificationService customerNotificationService;

    @Autowired
    private CustomerQueryService customerQueryService;

    @Test
    void shouldRegister() {
        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerQueryService.findById(customerId);

        Assertions.assertThat(customerOutput).extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getEmail,
                CustomerOutput::getBirthDate
        ).containsExactly(
                customerId,
                "Ronaldinho",
                "Gaúcho",
                "ronaldinho@gaucho.com",
                LocalDate.of(1980, 1,1)
        );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

        Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));
        Mockito.verify(customerEventListener, Mockito.never()).listen(Mockito.any(CustomerArchivedEvent.class));

        Mockito.verify(customerNotificationService).notifyNewRegistration(
                Mockito.any(CustomerNotificationService.NotifyNewRegistretionInput.class)
        );
    }

    @Test
    void shouldUpdate() {
        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        CustomerUpdateInput customerUpdateInput = CustomerUpdateInputTestDataBuilder.someCustomerUpdate().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId, customerUpdateInput);

        CustomerOutput customerOutput = customerQueryService.findById(customerId);

        Assertions.assertThat(customerOutput).extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getEmail,
                CustomerOutput::getBirthDate
        ).containsExactly(
                customerId,
                "Ronaldo",
                "Fenômeno",
                "ronaldinho@gaucho.com",
                LocalDate.of(1980, 1,1)
        );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    void shouldUpdateCustomerEmail() {
        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        String newEmail = "new@email.com";
        customerManagementApplicationService.changeEmail(customerId, newEmail);

        CustomerOutput customerOutput = customerQueryService.findById(customerId);

        Assertions.assertThat(customerOutput.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void shouldArchive() {
        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.archive(customerId);
        CustomerOutput customerOutput = customerQueryService.findById(customerId);

        Assertions.assertThat(customerOutput).extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getBirthDate,
                CustomerOutput::getDocument,
                CustomerOutput::getPhone,
                CustomerOutput::getArchived,
                CustomerOutput::getPromotionNotificationsAllowed
        ).containsExactly(
                customerId,
                "Anonymous",
                "Anonymous",
                null,
                "000-00-0000",
                "000-000-0000",
                true,
                false
        );
        Assertions.assertWith(customerOutput,
                c -> Assertions.assertThat(c.getArchivedAt()).isNotNull(),
                c -> Assertions.assertThat(c.getAddress().getNumber()).isEqualTo("Anonymous"),
                c -> Assertions.assertThat(c.getAddress().getComplement()).isNull()
        );
    }

    @Test
    void givenUnexistentCustomer_whenTryArchive_shouldGenerateException() {
        UUID customerId = new CustomerId().value();
        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class).isThrownBy(
                () -> customerManagementApplicationService.archive(customerId)
        );
    }

    @Test
    void givenUnexistentCustomer_whenTryChangeEmail_shouldGenerateException() {
        UUID customerId = new CustomerId().value();
        String newEmail = "new@email.com";
        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class).isThrownBy(
                () -> customerManagementApplicationService.changeEmail(customerId, newEmail)
        );
    }

    @Test
    void givenArchivedCustomer_whenTryArchive_shouldGenerateException() {
        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(
                () -> customerManagementApplicationService.archive(customerId)
        );
    }

    @Test
    void givenArchivedCustomer_whenTryChangeEmail_shouldGenerateException() {
        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.archive(customerId);
        String newEmail = "new@email.com";

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class).isThrownBy(
                () -> customerManagementApplicationService.changeEmail(customerId, newEmail)
        );
    }

    @Test
    void givenCustomer_whenTryChangeEmailWithInvalidEmail_shouldGenerateException() {
        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.archive(customerId);
        String newEmail = "invalid.email";

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> customerManagementApplicationService.changeEmail(customerId, newEmail)
        );
    }

    @Test
    void givenCustomer_whenTryChangeEmailToAnExistentEmail_shouldGenerateException() {
        String existentEmail = "existent@email.com";
        CustomerInput existent = CustomerInputTestDataBuilder.someCustomer().email(existentEmail).build();
        customerManagementApplicationService.create(existent);

        CustomerInput input = CustomerInputTestDataBuilder.someCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        Assertions.assertThatExceptionOfType(CustomerEmailIsInUseException.class).isThrownBy(
                () -> customerManagementApplicationService.changeEmail(customerId, existentEmail)
        );
    }
}