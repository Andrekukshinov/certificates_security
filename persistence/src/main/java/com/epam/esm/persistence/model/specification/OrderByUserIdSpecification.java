package com.epam.esm.persistence.model.specification;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class OrderByUserIdSpecification implements Specification<Order> {
    private final Long userId;

    public OrderByUserIdSpecification(Long userId) {
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<Order, User> joinedUser = root.join("user");
        return criteriaBuilder.equal(joinedUser.get("id"), userId);
    }
}
