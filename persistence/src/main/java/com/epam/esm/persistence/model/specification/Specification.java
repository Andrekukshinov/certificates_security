package com.epam.esm.persistence.model.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * Interface for building request predicates for searching in the data source
 *
 * @param <T> type to built predicate with
 */
public interface Specification<T> {
    /**
     * Builds predicate based on specification
     *
     * @param root            object to build predicate from
     * @param query           to build predicate for
     * @param criteriaBuilder to build predicate with
     * @return predicate build based on params
     */
    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

    /**
     * Method that joins 2 specifications with AND condition
     *
     * @param thisSpec first specification to be joined
     * @param other    second specification to be joined
     * @param <T>      type of new specification
     * @return new specification, that combines 2 predicates with AND condition
     */
    static <T> Specification<T> and(Specification<T> thisSpec, Specification<T> other) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        other.toPredicate(root, query, criteriaBuilder),
                        thisSpec.toPredicate(root, query, criteriaBuilder));

    }
}
