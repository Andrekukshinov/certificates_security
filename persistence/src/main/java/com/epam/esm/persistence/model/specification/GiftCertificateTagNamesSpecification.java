package com.epam.esm.persistence.model.specification;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GiftCertificateTagNamesSpecification implements Specification<GiftCertificate> {

    private final String tagName;

    public GiftCertificateTagNamesSpecification(@NonNull String tagName) {
        this.tagName = tagName;
    }

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<GiftCertificate, Tag> joinedTags = root.join("tags");
        return criteriaBuilder.equal(joinedTags.get("name"), tagName);
    }
}
