package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.enums.GiftCertificateStatus;
import com.epam.esm.persistence.exception.SortingException;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.CertificatesStatusSpecification;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import com.epam.esm.persistence.model.specification.Specification;
import com.epam.esm.persistence.repository.GiftCertificateRepository;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Repository
@Transactional
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String ACTIVE_CERTIFICATE_CREATE_DATE = "SELECT GC.createDate from GiftCertificate GC WHERE GC.id = :id AND GC.status ='ACTIVE'";

    @PersistenceContext
    private EntityManager manager;

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        manager.persist(giftCertificate);
        return giftCertificate;
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        Specification<GiftCertificate> byId = new FindByIdInSpecification<>(List.of(id));
        Specification<GiftCertificate> specification = Specification.and(byId, new CertificatesStatusSpecification(GiftCertificateStatus.ACTIVE));
        Page<GiftCertificate> certificate = findBySpecification(specification, Pageable.unpaged());
        GiftCertificate giftCertificate = DataAccessUtils.singleResult(certificate.getContent());
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public void delete(Long id) {
        findById(id).ifPresent(certificate -> {
            certificate.setStatus(GiftCertificateStatus.DELETED);
            manager.merge(certificate);
        });
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        Query query = manager.createQuery(ACTIVE_CERTIFICATE_CREATE_DATE)
                .setParameter("id", certificate.getId());
        LocalDateTime createDate = (LocalDateTime) query.getSingleResult();
        certificate.setCreateDate(createDate);
        manager.merge(certificate);
        return certificate;
    }

    @Override
    public GiftCertificate partialUpdate(Long certificateId, GiftCertificate toBeUpdated) {
        GiftCertificate found = manager.find(GiftCertificate.class, certificateId);
        setFieldsToUpdate(toBeUpdated, found);
        manager.merge(found);
        return found;
    }

    private void setFieldsToUpdate(GiftCertificate source, GiftCertificate target) {
        LocalDateTime createDate = source.getCreateDate();
        if (Objects.nonNull(createDate)) {
            target.setCreateDate(createDate);
        }
        LocalDateTime lastUpdateDate = source.getLastUpdateDate();
        if (Objects.nonNull(lastUpdateDate)) {
            target.setLastUpdateDate(lastUpdateDate);
        }
        Set<Tag> tags = source.getTags();
        if (Objects.nonNull(tags)) {
            target.setTags(tags);
        }
        String name = source.getName();
        if (Objects.nonNull(name)) {
            target.setName(name);
        }
        String description = source.getDescription();
        if (Objects.nonNull(description)) {
            target.setDescription(description);
        }
        BigDecimal price = source.getPrice();
        if (Objects.nonNull(price)) {
            target.setPrice(price);
        }
        GiftCertificateStatus status = source.getStatus();
        if (Objects.nonNull(status)) {
            target.setStatus(status);
        }
    }

    @Override
    public Page<GiftCertificate> findBySpecification(Specification<GiftCertificate> specification, Pageable pageable) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftCertificateFrom = query.from(GiftCertificate.class);
        Predicate restriction = specification.toPredicate(giftCertificateFrom, query, cb);
        query.where(restriction);
        TypedQuery<GiftCertificate> exec = getPagedQuery(pageable, cb, query, giftCertificateFrom);
        List<GiftCertificate> content = exec.getResultList();
        Integer lastPage = getLastPage(cb, pageable, specification);
        return new PageImpl<>(content, pageable, lastPage);
    }

    private Integer getLastPage(CriteriaBuilder cb, Pageable pageable, Specification<GiftCertificate> specification) {
        if (!pageable.isPaged()) {
            return 1;
        } else {
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<GiftCertificate> certificateRoot = countQuery.from(GiftCertificate.class);
            Predicate restriction = specification.toPredicate(certificateRoot, countQuery, cb);
            Expression<Long> count = cb.count(certificateRoot);
            countQuery = countQuery.select(count);
            countQuery = countQuery.where(restriction);
            Long totalAmount = manager.createQuery(countQuery).getSingleResult();
            Integer pageSize = pageable.getSize();
            int amount = (int) (totalAmount / pageSize);
            return totalAmount % pageSize == 0 ? amount : amount + 1;
        }
    }


    private TypedQuery<GiftCertificate> getPagedQuery(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<GiftCertificate> query, Root<GiftCertificate> from) {
        if (pageable.isPaged()) {
            int pageNumber = pageable.getPage();
            int pageSize = pageable.getSize();
            setSort(pageable, cb, query, from);
            TypedQuery<GiftCertificate> exec = manager.createQuery(query);
            exec.setFirstResult(pageNumber * pageSize);
            exec.setMaxResults(pageSize);
            return exec;
        }
        return manager.createQuery(query);
    }

    private void setSort(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<GiftCertificate> query, Root<GiftCertificate> from) {
        String sort = pageable.getSort();
        if (sort == null) {
            query.orderBy(cb.asc(from.get("id")));
        } else {
            Path<GiftCertificate> sortParam = getSortParam(from, sort);
            if ("desc".equalsIgnoreCase(pageable.getSortDir())) {
                query.orderBy(cb.desc(sortParam));
            } else {
                query.orderBy(cb.asc(sortParam));
            }
        }
    }

    private Path<GiftCertificate> getSortParam(Root<GiftCertificate> from, String sort) {
        try {
            return from.get(sort);
        } catch (IllegalArgumentException e) {
            throw new SortingException("sort value: " + sort + " is invalid");
        }
    }
}

