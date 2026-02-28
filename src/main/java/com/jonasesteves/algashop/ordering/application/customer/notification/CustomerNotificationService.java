package com.jonasesteves.algashop.ordering.application.customer.notification;

import java.util.UUID;

public interface CustomerNotificationService {
    void notifyNewRegistration(NotifyNewRegistretionInput input);

    record NotifyNewRegistretionInput(UUID customerUd, String firstName, String email){}
}
