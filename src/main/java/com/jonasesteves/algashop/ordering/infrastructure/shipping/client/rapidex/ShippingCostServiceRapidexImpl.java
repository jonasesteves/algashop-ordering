package com.jonasesteves.algashop.ordering.infrastructure.shipping.client.rapidex;

import com.jonasesteves.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.jonasesteves.algashop.ordering.domain.model.commons.Money;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(name = "algashop.integrations.shipping.provider", havingValue = "RAPIDEX")
public class ShippingCostServiceRapidexImpl implements ShippingCostService {

    private final RapidexApiClient client;

    public ShippingCostServiceRapidexImpl(RapidexApiClient client) {
        this.client = client;
    }

    @Override
    public CalculationResult calculate(CalculationRequest request) {
        DeliveryCostRequest deliveryCostRequest = new DeliveryCostRequest(request.origin().value(), request.destination().value());
        DeliveryCostResponse response = client.calculate(deliveryCostRequest);

        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(response.getEstimatedDaysToDelivery());
        return CalculationResult.builder()
                .cost(new Money(response.getDeliveryCost()))
                .expectedDate(expectedDeliveryDate)
                .build();
    }
}
