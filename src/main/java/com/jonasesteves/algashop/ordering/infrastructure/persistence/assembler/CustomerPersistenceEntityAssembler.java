package com.jonasesteves.algashop.ordering.infrastructure.persistence.assembler;

import com.jonasesteves.algashop.ordering.domain.model.entity.Customer;
import com.jonasesteves.algashop.ordering.domain.model.valueobject.Address;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.jonasesteves.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityAssembler {

    public CustomerPersistenceEntity fromDomain(Customer customer) {
        return merge(new CustomerPersistenceEntity(), customer);
    }

    public CustomerPersistenceEntity merge(CustomerPersistenceEntity customerPersistenceEntity, Customer customer) {
        customerPersistenceEntity.setId(customer.id().value());
        customerPersistenceEntity.setFirstName(customer.fullName().firstName());
        customerPersistenceEntity.setLastName(customer.fullName().lastName());
        customerPersistenceEntity.setBirthDate(customer.birthDate() != null ? customer.birthDate().value() : null);
        customerPersistenceEntity.setEmail(customer.email().value());
        customerPersistenceEntity.setPhone(customer.phone().value());
        customerPersistenceEntity.setDocument(customer.document().value());
        customerPersistenceEntity.setPromotionNotificationsAllowed(customer.isPromotionNotificationsAllowed());
        customerPersistenceEntity.setArchived(customer.isArchived());
        customerPersistenceEntity.setRegisteredAt(customer.registeredAt());
        customerPersistenceEntity.setArchivedAt(customer.archivedAt());
        customerPersistenceEntity.setLoyaltyPoints(customer.loyaltyPoints().value());
        customerPersistenceEntity.setAddress(toAddressEmbeddable(customer.address()));
        customerPersistenceEntity.setVersion(customer.version());
        return customerPersistenceEntity;
    }

    private AddressEmbeddable toAddressEmbeddable(Address address) {
        return new AddressEmbeddable(
                address.number(),
                address.street(),
                address.complement(),
                address.neighborhood(),
                address.city(),
                address.state(),
                address.zipCode().value()
        );
    }
}
