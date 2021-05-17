package com.epam.esm.persistence.model.specification;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.enums.GiftCertificateStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CertificatesStatusSpecification implements Specification<GiftCertificate> {
    private final GiftCertificateStatus status;

    public CertificatesStatusSpecification(GiftCertificateStatus status) {
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("status"), status);
    }
}
