package com.jonasesteves.algashop.ordering.infrastructure.notification.customer;

import com.jonasesteves.algashop.ordering.application.customer.notification.CustomerNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerNotificationServiceFakeImpl implements CustomerNotificationService {

    @Override
    public void notifyNewRegistration(NotifyNewRegistretionInput input) {
        log.info("Welcome {}, {}", input.firstName(), input.email());
    }
}
