package com.jonasesteves.algashop.ordering.infrastructure.fake;

import com.jonasesteves.algashop.ordering.domain.model.service.OriginAddressService;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Address;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.ZipCode;
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
