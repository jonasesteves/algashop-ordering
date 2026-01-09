package com.jonasesteves.algashop.ordering.domain.model.commons;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void shouldInstanceDocument() {
        Document document = new Document("1234-56-7890");
        Assertions.assertThat(document.value()).isEqualTo("1234-56-7890");
        Assertions.assertThat(document.toString()).isEqualTo("1234-56-7890");
    }

    @Test
    void shouldNotGenerateWithEmptyValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Document(""));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Document(" "));
    }
}