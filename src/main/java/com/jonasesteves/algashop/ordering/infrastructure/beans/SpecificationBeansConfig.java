package com.jonasesteves.algashop.ordering.infrastructure.beans;

import com.jonasesteves.algashop.ordering.domain.model.CustomerHaveFreeShippingSpecification;
import com.jonasesteves.algashop.ordering.domain.model.order.Orders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpecificationBeansConfig {
    /* M2 14.3 */
    @Bean
    public CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification(Orders orders) {
        return new CustomerHaveFreeShippingSpecification(
                100,
                2000,
                2L,
                orders
                );
    }
}
