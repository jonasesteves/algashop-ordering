package com.jonasesteves.algashop.ordering.application.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageFilter {
    private int size = 15;
    private int page = 0;
}