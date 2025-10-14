package com.jonasesteves.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void shouldInstanceEmail() {
        Email email = new Email("valid@email.com");
        Assertions.assertThat(email.value()).isEqualTo("valid@email.com");
        Assertions.assertThat(email.toString()).isEqualTo("valid@email.com");
    }

    @Test
    void shouldNotGenerateWithEmptyValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Email(""));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Email(" "));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Email("invalid@email"));
    }

}