package com.epam.esm.service.mapper;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.order.OrderDetailsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag map(TagDto source);
    TagDto map(Tag source);
}
