package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.PageImpl;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.persistence.repository.UserRepository;
import com.epam.esm.service.dto.user.UserInfoDto;
import com.epam.esm.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    private static final UserInfoDto USER_DTO = new UserInfoDto(1L, "email", "nick");

    private static final User USER = new User(1L, "pass", "email", "nick");

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByIdShouldReturnUserDtoWhenFound() {
        when(modelMapper.map(any(), any())).thenReturn(USER_DTO);
        when(userRepository.findById(any())).thenReturn(Optional.of(USER));

        UserInfoDto actual = orderService.getById(1L);

        assertThat(actual, is(USER_DTO));
    }

    @Test
    void testGetByIdWhenFoundShouldThrowExceptionWhenNotFound() {
        when(modelMapper.map(any(), any())).thenReturn(USER_DTO);

        assertThrows(EntityNotFoundException.class, () -> orderService.getById(1L));
    }

    @Test
    void testGetAllShouldReturnPageUserDtoWhenFound() {
        Page<User> page = new PageImpl<>(List.of(USER), Pageable.unpaged(), 1);
        Page<UserInfoDto> expected = new PageImpl<>(List.of(USER_DTO), Pageable.unpaged(), 1);
        when(modelMapper.map(any(), any())).thenReturn(USER_DTO);
        when(userRepository.findAll(any())).thenReturn(page);

        Page<UserInfoDto> actual = orderService.getAll(Pageable.unpaged());

        assertThat(actual, is(expected));
    }


}
