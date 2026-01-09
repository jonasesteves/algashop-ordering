package com.jonasesteves.algashop.ordering.application.checkout;

import com.jonasesteves.algashop.ordering.domain.model.commons.Money;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.jonasesteves.algashop.ordering.domain.model.customer.Customers;
import com.jonasesteves.algashop.ordering.domain.model.order.OrderId;
import com.jonasesteves.algashop.ordering.domain.model.order.Orders;
import com.jonasesteves.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.jonasesteves.algashop.ordering.domain.model.product.Product;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductCatalogService;
import com.jonasesteves.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Transactional
class BuyNowApplicationServiceIT {

    @Autowired
    private BuyNowApplicationService buyNowApplicationService;

    @Autowired
    private Orders orders;

    @Autowired
    private Customers customers;

    @MockitoBean
    private ProductCatalogService productCatalogService;

    @MockitoBean
    private ShippingCostService shippingCostService;

    @BeforeEach
    void setup() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldByNow() {
        Product product = ProductTestDataBuilder.someProduct().build();

        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));
        Mockito.when(shippingCostService.calculate(Mockito.any(ShippingCostService.CalculationRequest.class)))
                .thenReturn(new ShippingCostService.CalculationResult(new Money("10.00"), LocalDate.now().plusDays(3)));

        BuyNowInput input = BuyNowInputTestDataBuilder.aBuyNowInput().build();

        String orderId = buyNowApplicationService.buyNow(input);

        Assertions.assertThat(orderId).isNotBlank();
        Assertions.assertThat(orders.exists(new OrderId(orderId))).isTrue();
    }

}