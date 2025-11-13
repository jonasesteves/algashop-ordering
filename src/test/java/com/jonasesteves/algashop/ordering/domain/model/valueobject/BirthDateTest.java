package com.jonasesteves.algashop.ordering.domain.model.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class BirthDateTest {

    @Test
    void shouldInstanceNewBrithDate() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 1, 1));
        Assertions.assertThat(birthDate.value()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void givenDateAfterNow_whenInstance_shouldGenerateException() {
        LocalDate localDate = LocalDate.of(2100, 10, 10);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new BirthDate(localDate));
    }

    @Test
    void shuldReturnCorrectAge() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1990, 1, 1));

        int currentYear = LocalDate.now().getYear();
        int birthDateYear = birthDate.value().getYear();
        int age = currentYear - birthDateYear;

        Assertions.assertThat(birthDate.age()).isEqualTo(age);
    }

    @Test
    void shouldReturnCorrectBirthDateAsString() {
        BirthDate birthDate = new BirthDate(LocalDate.now());
        Assertions.assertThat(birthDate).hasToString(birthDate.toString());
    }

}