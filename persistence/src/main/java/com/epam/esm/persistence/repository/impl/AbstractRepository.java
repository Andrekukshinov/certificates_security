package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.exception.SortingException;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractRepository<T> {

    @PersistenceContext
    private EntityManager manager;

    public Optional<T> findById(Long orderId) {
        Specification<T> getById = new FindByIdInSpecification<>(List.of(orderId));
        return Optional.ofNullable(DataAccessUtils.singleResult(findBySpecification(getById, Pageable.unpaged()).getContent()));
    }

    public T save(T object) {
        manager.persist(object);
        return object;
    }

    public Page<T> findBySpecification(Specification<T> specification, Pageable pageable) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(getEntityClass());
        Root<T> giftCertificateFrom = query.from(getEntityClass());
        Predicate restriction = specification.toPredicate(giftCertificateFrom, query, cb);
        query.where(restriction);
        TypedQuery<T> exec = getPagedQuery(pageable, cb, query, giftCertificateFrom);
        List<T> content = exec.getResultList();
        Integer lastPage = getLastPage(cb, pageable, specification);
        return new PageImpl<>(content, pageable, lastPage);
    }

    private Integer getLastPage(CriteriaBuilder cb, Pageable pageable, Specification<T> specification) {
        if (!pageable.isPaged()) {
            return 1;
        } else {
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<T> certificateRoot = countQuery.from(getEntityClass());
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

    private TypedQuery<T> getPagedQuery(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<T> query, Root<T> from) {
        if (pageable.isPaged()) {
            int pageNumber = pageable.getPage();
            int pageSize = pageable.getSize();
            setSort(pageable, cb, query, from);
            TypedQuery<T> exec = manager.createQuery(query);
            int currentPage = pageNumber - 1;
            exec.setFirstResult(currentPage * pageSize);
            exec.setMaxResults(pageSize);
            return exec;
        }
        return manager.createQuery(query);
    }

    private void setSort(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<T> query, Root<T> from) {
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

    private Path<GiftCertificate> getSortParam(Root<T> from, String sort) {
        try {
            return from.get(sort);
        } catch (IllegalArgumentException e) {
            throw new SortingException("sort value: " + sort + " is invalid");
        }
    }

    protected abstract Class<T> getEntityClass();
}
