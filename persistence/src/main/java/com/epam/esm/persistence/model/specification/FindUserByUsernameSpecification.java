package com.epam.esm.persistence.model.specification;

import com.epam.esm.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindUserByUsernameSpecification implements Specification<User> {
    private final String username;

    public FindUserByUsernameSpecification(String username) {
        this.username = username;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("username"), username);
    }
}
