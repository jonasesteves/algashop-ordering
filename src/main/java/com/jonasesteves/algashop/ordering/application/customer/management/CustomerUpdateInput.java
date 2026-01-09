package com.jonasesteves.algashop.ordering.application.customer.management;

import com.jonasesteves.algashop.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUpdateInput {
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean promotionNotificationsAllowed;
    private AddressData address;
}
