package com.epam.esm.persistence.repository.impl;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.repository.OrderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class OrderRepositoryImpl extends AbstractRepository<Order> implements OrderRepository {
    @Override
    protected Class<Order> getEntityClass() {
        return Order.class;
    }
}
