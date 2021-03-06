package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

/**
 * Interface for executing operations with Tag entity within data source
 */
public interface TagRepository extends DeleteRepository<Tag>, CreateRepository<Tag> {

    /**
     * Method for searching for presence of tag in certificates
     *
     * @param id to look for tag with
     * @return optional of tag
     */
    Optional<Tag> findInCertificates(Long id);

    /**
     * Method for retrieving tag that was the most widely used by user with the highest cost of all orders
     *
     * @return tag
     */
    Tag getTopUserMostPopularTag();

    /**
     * Method for returning page with tags based on received specifications from data source
     *
     * @param specification to search and sort orders with
     * @param pageable      description of page retrieved
     * @return page with found orders
     */
    Page<Tag> findBySpecification(Specification<Tag> specification, Pageable pageable);

}
