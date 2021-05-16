package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.exception.SortingException;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.FindByIdInSpecification;
import com.epam.esm.persistence.model.specification.Specification;
import com.epam.esm.persistence.repository.TagRepository;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
public class TagRepositoryImpl implements TagRepository {
    private static final String ALL_TAGS = "SELECT T FROM Tag T";
    private static final String FIND_IN_CERTIFICATES =
            " SELECT tags.id, tags.name FROM tags" +
                    " INNER JOIN tags_gift_certificates tgc ON tags.id = tgc.tag_id" +
                    " WHERE tags.id = :id";

    private static final String FIND_MOST_POPULAR_TOP_USER_TAG =
                    "SELECT tg.name, tg.id, SUM(oc.quantity) total_amount\n" +
                    "FROM tags AS tg\n" +
                    "         INNER JOIN tags_gift_certificates tgc ON tg.id = tgc.tag_id\n" +
                    "         INNER JOIN gift_certificates gc ON tgc.gift_certificate_id = gc.id\n" +
                    "         INNER JOIN order_certificates oc ON gc.id = oc.certificate_id\n" +
                    "         INNER JOIN orders o ON oc.order_id = o.id\n" +
                    "WHERE o.id = (\n" +
                    "    SELECT user_id AS uid\n" +
                    "    FROM orders\n" +
                    "    GROUP BY uid\n" +
                    "    ORDER BY SUM(total_price) DESC\n" +
                    "    LIMIT 0,1\n" +
                    ")\n" +
                    "GROUP BY tg.name,  tg.id\n" +
                    "ORDER BY total_amount DESC\n" +
                    "LIMIT 0,1;";

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Tag save(Tag tag) {
        manager.persist(tag);
        return tag;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        Page<Tag> tags = find(new FindByIdInSpecification<>(List.of(id)), Pageable.unpaged());
        return Optional.ofNullable(DataAccessUtils.singleResult(tags.getContent()));
    }

    @Override
    public void delete(Long id) {
        Tag tag = manager.find(Tag.class, id);
        manager.remove(tag);
    }

    @Override
    public Optional<Tag> findInCertificates(Long id) {
        Query query = manager.createNativeQuery(FIND_IN_CERTIFICATES, Tag.class).setParameter("id", id);
        return Optional.ofNullable((Tag) DataAccessUtils.singleResult(query.getResultList()));
    }

    @Override
    public Page<Tag> find(Specification<Tag> specification, Pageable pageable) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = cb.createQuery(Tag.class);
        Root<Tag> tagFrom = query.from(Tag.class);
        Predicate restriction = specification.toPredicate(tagFrom, query, cb);
        query.where(restriction);
        Integer lastPage = getLastPage(cb, pageable, specification);
        TypedQuery<Tag> exec = getPagedQuery(pageable, cb, query, tagFrom);
        List<Tag> resultList = exec.getResultList();
        return new PageImpl<>(resultList, pageable, lastPage);
    }

    @Override
    public Tag getTopUserMostPopularTag() {
        Query nativeQuery = manager.createNativeQuery(FIND_MOST_POPULAR_TOP_USER_TAG, Tag.class);
        return (Tag) DataAccessUtils.singleResult(nativeQuery.getResultList());
    }


    private Integer getLastPage(CriteriaBuilder cb, Pageable pageable, Specification<Tag> specification) {
        if (!pageable.isPaged()) {
            return 1;
        } else {
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Tag> certificateRoot = countQuery.from(Tag.class);
            countQuery.select(cb.count(certificateRoot));
            Predicate restriction = specification.toPredicate(certificateRoot, countQuery, cb);
            countQuery.where(restriction);
            Long totalAmount = manager.createQuery(countQuery).getSingleResult();
            Integer pageSize = pageable.getSize();
            int amount = (int) (totalAmount / pageSize);
            return totalAmount % pageSize == 0 ? amount : amount + 1;
        }
    }


    private TypedQuery<Tag> getPagedQuery(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<Tag> query, Root<Tag> from) {
        if (pageable.isPaged()) {
            int pageNumber = pageable.getPage();
            int pageSize = pageable.getSize();
            setSort(pageable, cb, query, from);
            TypedQuery<Tag> exec = manager.createQuery(query);
            exec.setFirstResult(pageNumber * pageSize);
            exec.setMaxResults(pageSize);
            return exec;
        }
        return manager.createQuery(query);
    }

    private void setSort(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<Tag> query, Root<Tag> from) {
        String sort = pageable.getSort();
        if (sort == null) {
            query.orderBy(cb.asc(from.get("id")));
        } else {
            Path<Tag> sortParam = getSortParam(from, sort);
            if ("desc".equalsIgnoreCase(pageable.getSortDir())) {
                query.orderBy(cb.desc(sortParam));
            } else {
                query.orderBy(cb.asc(sortParam));
            }
        }
    }

    private Path<Tag> getSortParam(Root<Tag> from, String sort) {
        try {
            return from.get(sort);
        } catch (IllegalArgumentException e) {
            throw new SortingException("sort value: " + sort + " is invalid");
        }
    }
}
