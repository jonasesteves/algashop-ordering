package com.jonasesteves.algashop.ordering.infrastructure.listener.customer;

import com.jonasesteves.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.jonasesteves.algashop.ordering.application.customer.notification.CustomerNotificationService.NotifyNewRegistretionInput;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerEventListener {

    private final CustomerNotificationService customerNotificationService;

    public CustomerEventListener(CustomerNotificationService customerNotificationService) {
        this.customerNotificationService = customerNotificationService;
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

}
