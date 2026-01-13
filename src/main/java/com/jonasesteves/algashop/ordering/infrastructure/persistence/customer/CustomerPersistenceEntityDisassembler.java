package com.jonasesteves.algashop.ordering.infrastructure.persistence.customer;

import com.jonasesteves.algashop.ordering.domain.model.customer.Customer;
import com.jonasesteves.algashop.ordering.domain.model.commons.Address;
import com.jonasesteves.algashop.ordering.domain.model.customer.BirthDate;
import com.jonasesteves.algashop.ordering.domain.model.commons.Document;
import com.jonasesteves.algashop.ordering.domain.model.commons.Email;
import com.jonasesteves.algashop.ordering.domain.model.commons.FullName;
import com.jonasesteves.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.jonasesteves.algashop.ordering.domain.model.commons.Phone;
import com.jonasesteves.algashop.ordering.domain.model.commons.ZipCode;
import com.jonasesteves.algashop.ordering.domain.model.customer.CustomerId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityDisassembler {

    public Customer toDomain(CustomerPersistenceEntity entity) {
        return Customer.existing()
                .id(new CustomerId(entity.getId()))
                .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
                .birthDate(entity.getBirthDate() != null ? new BirthDate(entity.getBirthDate()) : null)
                .email(new Email(entity.getEmail()))
                .phone(new Phone(entity.getPhone()))
                .document(new Document(entity.getDocument()))
                .promotionNotificationsAllowed(entity.getPromotionNotificationsAllowed())
                .archived(entity.getArchived())
                .registeredAt(entity.getRegisteredAt())
                .archivedAt(entity.getArchivedAt())
                .loyaltyPoints(new LoyaltyPoints(entity.getLoyaltyPoints()))
                .address(this.address(entity.getAddress()))
                .version(entity.getVersion())
                .build();
    }

    private Address address(AddressEmbeddable addressEmbeddable) {
        return Address.builder()
                .number(addressEmbeddable.getNumber())
                .street(addressEmbeddable.getStreet())
                .complement(addressEmbeddable.getComplement())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }
}
