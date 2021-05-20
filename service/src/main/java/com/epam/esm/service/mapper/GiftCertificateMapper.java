package com.epam.esm.service.mapper;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.service.dto.certificate.GiftCertificateTagDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GiftCertificateMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void map(GiftCertificateTagDto source, @MappingTarget GiftCertificate destination);
    GiftCertificate map(GiftCertificateTagDto source);
    GiftCertificateTagDto map(GiftCertificate source);
}
