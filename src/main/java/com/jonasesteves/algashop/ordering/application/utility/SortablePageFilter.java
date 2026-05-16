package com.jonasesteves.algashop.ordering.application.utility;


import org.springframework.data.domain.Sort;

import java.util.Objects;

public abstract class SortablePageFilter<T> extends PageFilter {
    private T sortByProperty;
    private Sort.Direction sortDirection;

    protected SortablePageFilter(){

    }

    protected SortablePageFilter(int size, int page, T sortByProperty, Sort.Direction sortDirection) {
        super(size, page);
        this.sortByProperty = sortByProperty;
        this.sortDirection = sortDirection;
    }

    protected SortablePageFilter(T sortByProperty, Sort.Direction sortDirection) {
        this.sortByProperty = sortByProperty;
        this.sortDirection = sortDirection;
    }

    protected SortablePageFilter(int size, int page) {
        super(size, page);
    }

    public T getSortByProperty() {
        return sortByProperty;
    }

    public void setSortByProperty(T sortByProperty) {
        this.sortByProperty = sortByProperty;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public abstract T getSortByPropertyOrDefault();

    public abstract Sort.Direction getSortDirectionOrDefault();

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        SortablePageFilter<?> that = (SortablePageFilter<?>) object;
        return Objects.equals(sortByProperty, that.sortByProperty) && sortDirection == that.sortDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sortByProperty, sortDirection);
    }
    @Override
    public String toString() {
        return "SortablePageFilter{" +
                "sortByProperty=" + sortByProperty +
                ", sortDirection=" + sortDirection +
                '}';
    }
}
