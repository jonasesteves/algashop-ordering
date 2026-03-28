package com.jonasesteves.algashop.ordering.infrastructure.listener.shoppingcart;

import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartCreatedEvent;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartEmptiedEvent;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemAddedEvent;
import com.jonasesteves.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemRemovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShoppingCartEventListener {

    @EventListener
    public void listen(ShoppingCartCreatedEvent event) {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        log.info("ShoppingCartCreatedEvent");
    }

    @EventListener
    public void listen(ShoppingCartEmptiedEvent event) {

    }

    @EventListener
    public void listen(ShoppingCartItemAddedEvent event) {

    }

    @EventListener
    public void listen(ShoppingCartItemRemovedEvent event) {

    }
}
