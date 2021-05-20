package com.epam.esm.service.mapper;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.service.dto.order.OrderCertificatesDto;
import com.epam.esm.service.dto.order.OrderDetailsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order map(OrderDetailsDto source);
    OrderDetailsDto map(Order source);

}
