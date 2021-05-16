package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.persistence.exception.SortingException;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.FindAllSpecification;
import com.epam.esm.persistence.model.specification.Specification;
import com.epam.esm.persistence.repository.UserRepository;
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
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<User> findById(Long id) {
        User value = manager.find(User.class, id);
        return Optional.ofNullable(value);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> from = query.from(User.class);
        query.select(from);
        TypedQuery<User> exec = getPagedQuery(pageable, cb, query, from);
        Integer lastPage = getLastPage(cb, pageable, new FindAllSpecification<>());
        List<User> resultList = exec.getResultList();
        return new PageImpl<>(resultList, pageable, lastPage);
    }

    private Integer getLastPage(CriteriaBuilder cb, Pageable pageable, Specification<User> specification) {
        if (!pageable.isPaged()) {
            return 1;
        } else {
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<User> certificateRoot = countQuery.from(User.class);
            countQuery.select(cb.count(certificateRoot));
            Predicate restriction = specification.toPredicate(certificateRoot, countQuery, cb);
            countQuery.where(restriction);
            Long totalAmount = manager.createQuery(countQuery).getSingleResult();
            Integer pageSize = pageable.getSize();
            int amount = (int) (totalAmount / pageSize);
            return totalAmount % pageSize == 0 ? amount : amount + 1;
        }
    }

    private TypedQuery<User> getPagedQuery(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<User> query, Root<User> from) {
        if (pageable.isPaged()) {
            int pageNumber = pageable.getPage();
            int pageSize = pageable.getSize();
            setSort(pageable, cb, query, from);
            TypedQuery<User> exec = manager.createQuery(query);
            exec.setFirstResult(pageNumber * pageSize);
            exec.setMaxResults(pageSize);
            return exec;
        }
        return manager.createQuery(query);
    }

    private void setSort(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<User> query, Root<User> from) {
        String sort = pageable.getSort();
        if (sort == null) {
            query.orderBy(cb.asc(from.get("id")));
        } else {
            Path<User> sortParam = getSortParam(from, sort);
            if ("desc".equalsIgnoreCase(pageable.getSortDir())) {
                query.orderBy(cb.desc(sortParam));
            } else {
                query.orderBy(cb.asc(sortParam));
            }
        }
    }

    private Path<User> getSortParam(Root<User> from, String sort) {
        try {
            return from.get(sort);
        } catch (IllegalArgumentException e) {
            throw new SortingException("sort value: " + sort + " is invalid");
        }
    }
}
