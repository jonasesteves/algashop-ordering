package com.jonasesteves.algashop.ordering.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressData {
    private String street;
    private String number;
    private String city;
    private String state;
    private String zipCode;
    private String complement;
    private String neighborhood;
}
