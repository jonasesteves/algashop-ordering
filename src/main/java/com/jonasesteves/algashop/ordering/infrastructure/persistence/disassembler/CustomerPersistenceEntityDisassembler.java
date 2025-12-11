package com.jonasesteves.algashop.ordering.infrastructure.persistence.disassembler;

import com.jonasesteves.algashop.ordering.domain.model.entity.Customer;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Address;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.BirthDate;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Document;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Email;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.FullName;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Phone;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.ZipCode;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityDisassembler {

    public Customer toDomain(CustomerPersistenceEntity customerPersistenceEntity) {
        return Customer.existing()
                .id(new CustomerId(customerPersistenceEntity.getId()))
                .fullName(new FullName(customerPersistenceEntity.getFirstName(), customerPersistenceEntity.getLastName()))
                .birthDate(new BirthDate(customerPersistenceEntity.getBirthDate()))
                .email(new Email(customerPersistenceEntity.getEmail()))
                .phone(new Phone(customerPersistenceEntity.getPhone()))
                .document(new Document(customerPersistenceEntity.getDocument()))
                .promotionNotificationsAllowed(customerPersistenceEntity.getPromotionNotificationsAllowed())
                .archived(customerPersistenceEntity.getArchived())
                .registeredAt(customerPersistenceEntity.getRegisteredAt())
                .archivedAt(customerPersistenceEntity.getArchivedAt())
                .loyaltyPoints(new LoyaltyPoints(customerPersistenceEntity.getLoyaltyPoints()))
                .address(this.address(customerPersistenceEntity.getAddress()))
                .version(customerPersistenceEntity.getVersion())
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
