package com.epam.esm.service.mapper;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.user.UserInfoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User map(UserInfoDto source);
    UserInfoDto map(User source);
}
