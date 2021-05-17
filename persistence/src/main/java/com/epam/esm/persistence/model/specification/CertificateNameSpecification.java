package com.epam.esm.persistence.model.specification;

import com.epam.esm.persistence.entity.GiftCertificate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CertificateNameSpecification implements Specification<GiftCertificate> {
    private final String certificateName;

    public CertificateNameSpecification(String certificateName) {
        this.certificateName = certificateName;
    }

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(root.get("name"), "%" + certificateName + "%");
    }
}
