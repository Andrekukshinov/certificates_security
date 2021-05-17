package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface for executing operations with order entity within data source
 */
public interface UserRepository extends ReadOperationRepository<User> {

    /**
     * Method for returning page with users from data source
     *
     * @param pageable description of page retrieved
     * @return page with found users
     */
    Page<User> findBySpecification(Specification<User> specification, Pageable pageable);
}
