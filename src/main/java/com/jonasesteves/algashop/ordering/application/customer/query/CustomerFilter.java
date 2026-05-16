package com.jonasesteves.algashop.ordering.application.customer.query;

import com.jonasesteves.algashop.ordering.application.utility.SortablePageFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerFilter extends SortablePageFilter<CustomerFilter.SortType> {

    private String email;
    private String firstName;

    public CustomerFilter(int size, int page) {
        super(size, page);
    }

    @Override
    public SortType getSortByPropertyOrDefault() {
        return getSortByProperty() == null ? SortType.REGISTERED_AT : getSortByProperty();
    }

    @Override
    public Sort.Direction getSortDirectionOrDefault() {
        return getSortDirection() == null ? Sort.Direction.ASC : getSortDirection();
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        REGISTERED_AT("registeredAt"),
        FIRST_NAME("firstName");

        private final String propertyName;
    }
}
