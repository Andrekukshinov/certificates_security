package com.epam.esm.service.mapper;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.service.dto.order.OrderCertificatesDto;
import com.epam.esm.service.dto.order.OrderDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "userId", target = "user.id")
    Order map(OrderCertificatesDto source);
    OrderCertificatesDto map(Order source);

    Order map(OrderDetailsDto source);
    OrderDetailsDto mapToOrderDetails(Order source);

}
