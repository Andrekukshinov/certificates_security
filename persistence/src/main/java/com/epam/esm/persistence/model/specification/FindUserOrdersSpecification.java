package com.epam.esm.persistence.model.specification;

import com.epam.esm.persistence.entity.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindUserOrdersSpecification implements Specification<Order> {
    private final Long userId;

    public FindUserOrdersSpecification(Long userId) {
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}
