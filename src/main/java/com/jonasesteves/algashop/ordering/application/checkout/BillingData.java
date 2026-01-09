package com.jonasesteves.algashop.ordering.application.checkout;

import com.jonasesteves.algashop.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingData {
    private String firstName;
    private String lastName;
    private String document;
    private String phone;
    private String email;
    private AddressData address;
}
