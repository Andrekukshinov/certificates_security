package com.epam.esm.service.mapper;

import com.epam.esm.persistence.entity.OrderCertificate;
import com.epam.esm.service.dto.order.OrderCertificateUnitDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderUnitMapper {
    OrderCertificate map(OrderCertificateUnitDto source);
    OrderCertificateUnitDto map(OrderCertificate source);
}
