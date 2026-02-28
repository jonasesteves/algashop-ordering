package com.jonasesteves.algashop.ordering.infrastructure.listener.order;

import com.jonasesteves.algashop.ordering.domain.model.order.OrderCanceledEvent;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderPaidEvent;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderPlacedEvent;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderReadyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventListener {

    @EventListener
    public void listen(OrderPlacedEvent event) {
        log.info("OrderPlacedEvent: order marked as placed successfuly");
    }

    @EventListener
    public void listen(OrderReadyEvent event) {
        log.info("OrderReadyEvent:  order marked as ready successfuly");
    }

    @EventListener
    public void listen(OrderPaidEvent event) {
        log.info("OrderPaidEvent:  order marked as paid successfuly");
    }

    @EventListener
    public void listen(OrderCanceledEvent event) {
        log.info("OrderCanceledEvent:   order marked as canceled successfuly");
    }
}
