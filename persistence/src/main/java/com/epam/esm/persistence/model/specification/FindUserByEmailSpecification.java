package com.epam.esm.persistence.model.specification;

import com.epam.esm.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindUserByEmailSpecification implements Specification<User> {
    private final String email;

    public FindUserByEmailSpecification(String email) {
        this.email = email;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("email"), email);
    }
}
