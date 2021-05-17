package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.repository.TagRepository;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
@Transactional
public class TagRepositoryImpl extends AbstractRepository<Tag> implements TagRepository {
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
    public Tag getTopUserMostPopularTag() {
        Query nativeQuery = manager.createNativeQuery(FIND_MOST_POPULAR_TOP_USER_TAG, Tag.class);
        return (Tag) DataAccessUtils.singleResult(nativeQuery.getResultList());
    }


    @Override
    protected Class<Tag> getEntityClass() {
        return Tag.class;
    }
}
