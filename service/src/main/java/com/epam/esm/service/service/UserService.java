package com.epam.esm.service.service;

import com.epam.esm.service.dto.user.UserInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface for performing business logics for users and user Dtos
 */
public interface UserService {
    /**
     * Method that returns UserInfoDto dto based on received id
     *
     * @param id to find object with
     * @return UserInfoDto dto entity with specified id
     */
    UserInfoDto getById(Long id);

    /**
     * Method for returning list of all tag dtos that are present in the system
     *
     * @return list of tag dtos
     */
    Page<UserInfoDto> getAll(Pageable pageable);
}
