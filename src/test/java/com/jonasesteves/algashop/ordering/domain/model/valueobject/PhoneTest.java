package com.jonasesteves.algashop.ordering.domain.model.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PhoneTest {

    @Test
    void shouldInstancePhone() {
        Phone phone = new Phone("000-111-2222");
        Assertions.assertThat(phone.value()).isEqualTo("000-111-2222");
        Assertions.assertThat(phone.toString()).isEqualTo("000-111-2222");
    }

    @Test
    void shouldNotGenerateWithEmptyValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Phone(""));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Phone(" "));
    }

}