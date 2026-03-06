package com.jonasesteves.algashop.ordering.infrastructure.listener.customer;

import com.jonasesteves.algashop.ordering.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.jonasesteves.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.jonasesteves.algashop.ordering.domain.model.commons.Email;
import com.jonasesteves.algashop.ordering.domain.model.commons.FullName;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderId;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderReadyEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.OffsetDateTime;
import java.util.UUID;

@SpringBootTest
class CustomerEventListenerIT {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoBean
    private CustomerLoyaltyPointsApplicationService  customerLoyaltyPointsApplicationService;

    @MockitoSpyBean
    private CustomerNotificationService  customerNotificationService;

    @Test
    void shouldListenOrderReadyEvent() {
        applicationEventPublisher.publishEvent(new OrderReadyEvent(new OrderId(), new CustomerId(), OffsetDateTime.now()));

        Mockito.verify(customerEventListener).listen(Mockito.any(OrderReadyEvent.class));
        Mockito.verify(customerLoyaltyPointsApplicationService).addLoyaltyPoints(Mockito.any(UUID.class), Mockito.any(String.class));
    }

    @Test
    void shouldListenCustomerRegisteredEvent() {
        applicationEventPublisher.publishEvent(new CustomerRegisteredEvent(
                new CustomerId(),
                OffsetDateTime.now(),
                new FullName("Mark", "Zuck"),
                new Email("mark@zuck.book")
        ));

        Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));
        Mockito.verify(customerNotificationService).notifyNewRegistration(Mockito.any(CustomerNotificationService.NotifyNewRegistretionInput.class));
    }
}