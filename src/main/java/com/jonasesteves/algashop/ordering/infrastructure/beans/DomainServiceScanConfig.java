package com.jonasesteves.algashop.ordering.infrastructure.beans;

import com.jonasesteves.algashop.ordering.domain.model.utility.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "com.jonasesteves.algashop.ordering.domain.model",
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = DomainService.class)
)
public class DomainServiceScanConfig {
    /* Configurar Spring para carregar as classes com essas annotations e transformar em beans. (M2 9.8) */
}
