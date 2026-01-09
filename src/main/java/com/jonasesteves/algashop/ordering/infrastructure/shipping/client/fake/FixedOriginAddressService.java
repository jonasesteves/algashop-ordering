package com.jonasesteves.algashop.ordering.infrastructure.shipping.client.fake;

import com.jonasesteves.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.jonasesteves.algashop.ordering.domain.model.commons.Address;
import com.jonasesteves.algashop.ordering.domain.model.commons.ZipCode;
import org.springframework.stereotype.Component;

@Component
public class FixedOriginAddressService implements OriginAddressService {
    @Override
    public Address originAddress() {
        return Address.builder()
                .number("1525")
                .street("Algashop Street")
                .neighborhood("Gaia Beach")
                .city("San Valentin")
                .state("Port Hammer")
                .zipCode(new ZipCode("44608"))
                .build();
    }
}
