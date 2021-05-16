package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.model.specification.Specification;

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
    Page<Order> find(Specification<Order> specification, Pageable pageable);
}
