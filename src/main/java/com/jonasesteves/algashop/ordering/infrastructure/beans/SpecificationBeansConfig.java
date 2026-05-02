package com.jonasesteves.algashop.ordering.infrastructure.beans;

import com.jonasesteves.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.jonasesteves.algashop.ordering.domain.model.order.CustomerHasFreeShippingSpecification;
import com.jonasesteves.algashop.ordering.domain.model.order.Orders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpecificationBeansConfig {
    /* M2 14.3 */
    @Bean
    public CustomerHasFreeShippingSpecification customerHaveFreeShippingSpecification(Orders orders) {
        return new CustomerHasFreeShippingSpecification(
                orders,
                new LoyaltyPoints(100),
                2L,
                new LoyaltyPoints(2000)
        );
    }
}
