package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.exception.SortingException;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import com.epam.esm.persistence.model.specification.Specification;
import com.epam.esm.persistence.repository.OrderRepository;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Order save(Order order) {
        manager.persist(order);
        return order;
    }

    @Override
    public Page<Order> find(Specification<Order> specification, Pageable pageable) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> orderFrom = query.from(Order.class);
        Predicate restriction = specification.toPredicate(orderFrom, query, cb);
        query.where(restriction);
        Integer lastPage = getLastPage(cb, pageable, specification);
        TypedQuery<Order> exec = getPagedQuery(pageable, cb, query, orderFrom);
        List<Order> resultList = exec.getResultList();
        return new PageImpl<>(resultList, pageable, lastPage);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        Specification<Order> getById = new FindByIdInSpecification<>(List.of(orderId));
        return Optional.ofNullable(DataAccessUtils.singleResult(find(getById, Pageable.unpaged()).getContent()));
    }

    private Integer getLastPage(CriteriaBuilder cb, Pageable pageable, Specification<Order> specification) {
        if (!pageable.isPaged()) {
            return 1;
        } else {
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Order> certificateRoot = countQuery.from(Order.class);
            countQuery.select(cb.count(certificateRoot));
            Predicate restriction = specification.toPredicate(certificateRoot, countQuery, cb);
            countQuery.where(restriction);
            Long totalAmount = manager.createQuery(countQuery).getSingleResult();
            Integer pageSize = pageable.getSize();
            int amount = (int) (totalAmount / pageSize);
            return totalAmount % pageSize == 0 ? amount : amount + 1;
        }
    }


    private TypedQuery<Order> getPagedQuery(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<Order> query, Root<Order> from) {
        if (pageable.isPaged()) {
            int pageNumber = pageable.getPage();
            int pageSize = pageable.getSize();
            setSort(pageable, cb, query, from);
            TypedQuery<Order> exec = manager.createQuery(query);
            exec.setFirstResult(pageNumber * pageSize);
            exec.setMaxResults(pageSize);
            return exec;
        }
        return manager.createQuery(query);
    }

    private void setSort(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<Order> query, Root<Order> from) {
        String sort = pageable.getSort();
        if (sort == null) {
            query.orderBy(cb.asc(from.get("id")));
        } else {
            Path<Order> sortParam = getSortParam(from, sort);
            if ("desc".equalsIgnoreCase(pageable.getSortDir())) {
                query.orderBy(cb.desc(sortParam));
            } else {
                query.orderBy(cb.asc(sortParam));
            }
        }
    }

    private Path<Order> getSortParam(Root<Order> from, String sort) {
        try {
            return from.get(sort);
        } catch (IllegalArgumentException e) {
            throw new SortingException("sort value: " + sort + " is invalid");
        }
    }
}
