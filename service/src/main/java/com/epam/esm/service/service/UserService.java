package com.epam.esm.service.service;

import com.epam.esm.service.dto.user.UserInfoDto;
import com.epam.esm.service.dto.user.UserRegistrationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * Interface for performing business logics for users and user Dtos
 */
public interface UserService extends UserDetailsService {
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

    Optional<UserInfoDto> findByUsername(String username);

    UserInfoDto registerUser(UserRegistrationModel user);
}
