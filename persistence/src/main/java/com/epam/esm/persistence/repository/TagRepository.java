package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Interface for executing operations with Tag entity within data source
 */
@Repository
@Transactional
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    /**
     * Method for searching for presence of tag in certificates
     *
     * @param id to look for tag with
     * @return optional of tag
     */
    @Query(value = ("SELECT tags.id, tags.name FROM tags" +
            " INNER JOIN tags_gift_certificates tgc ON tags.id = tgc.tag_id" +
            " WHERE tags.id = :id"),nativeQuery = true)
    Optional<Tag> findInCertificates(@Param("id") Long id);

    /**
     * Method for retrieving tag that was the most widely used by user with the highest cost of all orders
     *
     * @return tag
     */
    @Query(value = ("SELECT tg.name, tg.id, SUM(oc.quantity) total_amount\n" +
            "FROM tags AS tg " +
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
            "LIMIT 0,1"), nativeQuery = true)
    Tag getTopUserMostPopularTag();


}
