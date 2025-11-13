package com.jonasesteves.algashop.ordering.domain.model.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void shouldInstanceEmail() {
        Email email = new Email("valid@email.com");
        Assertions.assertThat(email.value()).isEqualTo("valid@email.com");
        Assertions.assertThat(email).hasToString("valid@email.com");
    }

    @Test
    void shouldNotGenerateWithEmptyValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Email(""));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Email(" "));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Email("invalid@email"));
    }

}