package com.epam.esm.persistence.model.specification;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.enums.GiftCertificateStatus;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FindAllActiveCertificatesSpecification implements Specification<GiftCertificate> {
    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.and(
                (cb.or(cb.ge(root.get("id"), 0), cb.le(root.get("id"), 0))),
                (cb.equal(root.get("status"), GiftCertificateStatus.ACTIVE)));
    }
}
