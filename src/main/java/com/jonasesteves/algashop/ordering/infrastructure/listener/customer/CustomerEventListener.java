package com.jonasesteves.algashop.ordering.infrastructure.listener.customer;

import com.jonasesteves.algashop.ordering.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.jonasesteves.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.jonasesteves.algashop.ordering.application.customer.notification.CustomerNotificationService.NotifyNewRegistretionInput;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderReadyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerEventListener {

    private final CustomerNotificationService customerNotificationService;
    private final CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

    public CustomerEventListener(CustomerNotificationService customerNotificationService, CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService) {
        this.customerNotificationService = customerNotificationService;
        this.customerLoyaltyPointsApplicationService = customerLoyaltyPointsApplicationService;
    }

    @EventListener
    public void listen(CustomerRegisteredEvent event) {
        log.info("CustomerRegisteredEvent listen 1");
        NotifyNewRegistretionInput input = new NotifyNewRegistretionInput(event.customerId().value(), event.fullName().firstName(), event.email().value());
        customerNotificationService.notifyNewRegistration(input);
    }

    @EventListener
    public void listen(CustomerArchivedEvent event) {
        log.info("CustomerArchivedEvent listen 1");
    }

    @EventListener
    public void listen(OrderReadyEvent  event) {
        customerLoyaltyPointsApplicationService.addLoyaltyPoints(event.customerId().value(), event.orderId().toString());
    }

}
