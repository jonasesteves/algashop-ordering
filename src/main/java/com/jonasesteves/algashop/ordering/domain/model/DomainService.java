package com.jonasesteves.algashop.ordering.domain.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DomainService {
    /* Evita a adição de novas dependências (neste caso a dependência do Spring Framework)
    no domain model, além das que já existem como Lombok e UUID7. (M2 9.8) */
}
