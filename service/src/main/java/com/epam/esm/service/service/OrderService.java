package com.epam.esm.service.service;

import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.service.dto.order.OrderCertificatesDto;
import com.epam.esm.service.dto.order.OrderDetailsDto;

/**
 * Interface for performing business logics for OrderCertificatesDto and OrderDetailsDto
 */
public interface OrderService {
    /**
     * Method that performs saving of orderCertificatesDto
     *
     * @param orderCertificatesDto dto to be performed logics with
     * @return certificateDto dto saved
     */
    OrderCertificatesDto save(OrderCertificatesDto orderCertificatesDto);

    /**
     * Method for retrieving user order
     *
     * @param userId  to look for user to find order from
     * @param orderId to find order from found user
     * @return dto entity if such user order exists order
     */
    OrderCertificatesDto getUserOrderById(Long userId, Long orderId);

    /**
     * Method for retrieving all user order
     *
     * @param userId   to look for user to find order from
     * @param pageable to build page with orders
     * @return page with dto entities
     */
    Page<OrderDetailsDto> getAllUserOrders(Long userId, Pageable pageable);
}
