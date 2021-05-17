package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface for executing operations with order entity within data source
 */
public interface OrderRepository extends ReadOperationRepository<Order>, CreateRepository<Order> {

    /**
     * Method for returning page with orders based on received specifications from data source
     *
     * @param specification to search and sort orders with
     * @param pageable      description of page retrieved
     * @return page with found orders
     */
    Page<Order> findBySpecification(Specification<Order> specification, Pageable pageable);
}
