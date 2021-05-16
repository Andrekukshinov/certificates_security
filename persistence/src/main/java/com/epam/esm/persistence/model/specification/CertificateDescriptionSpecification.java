package com.epam.esm.persistence.model.specification;

import com.epam.esm.persistence.entity.GiftCertificate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CertificateDescriptionSpecification implements Specification<GiftCertificate> {
    private final String certificateDescription;

    public CertificateDescriptionSpecification(String certificateDescription) {
        this.certificateDescription = certificateDescription;
    }

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(root.get("description"), ("%" + certificateDescription + "%"));
    }
}
