package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.exception.SortingException;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.mapping.PropertyReferenceException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
        query.where(specification.toPredicate(giftCertificateFrom, query, cb));
        TypedQuery<T> exec = getPagedQuery(pageable, cb, query, giftCertificateFrom);
        Long lastPage = getLastPage(cb, specification);
        List<T> content = exec.getResultList();
        return new PageImpl<>(content, pageable, lastPage);
    }

//    private Long getLastPage(CriteriaBuilder cb, Specification<T> specification) {
//        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//        countQuery.select(cb.count(countQuery.from(getEntityClass())));
//        return manager.createQuery(countQuery).getSingleResult();
//    }

    private Long getLastPage(CriteriaBuilder cb, Specification<T> specification) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> root = countQuery.from(getEntityClass());
        Predicate predicate = specification.toPredicate(root, countQuery, cb);
        countQuery.where(predicate);
        countQuery.select(cb.count(root));
        Long singleResult = manager.createQuery(countQuery).getSingleResult();
        return singleResult;
    }

    private TypedQuery<T> getPagedQuery(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<T> query, Root<T> from) {
        if (pageable.isPaged()) {
            long pageNumber = pageable.getOffset();
            int pageSize = pageable.getPageSize();
            try {
                query.orderBy((QueryUtils.toOrders(pageable.getSort(), from, cb)));
            } catch (PropertyReferenceException e) {
                throw new SortingException(e.getMessage());
            }
            TypedQuery<T> exec = manager.createQuery(query);
            exec.setFirstResult(Math.toIntExact(pageNumber));
            exec.setMaxResults((pageSize));
            return exec;
        }
        return manager.createQuery(query);
    }

    protected abstract Class<T> getEntityClass();
}
