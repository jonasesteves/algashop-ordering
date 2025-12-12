package com.jonasesteves.algashop.ordering;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class teste {
    public static void main(String[] args) {
        List<Integer> lista = List.of(14, 5, 6, 13, 76, 45, 42, 28, 34, 1, 2, 59, 50);

        List<Integer> ordered = lista.stream()
                .filter(n -> n % 2 == 0)
                .sorted()
                .toList();

        System.out.println(ordered);
    }
}
