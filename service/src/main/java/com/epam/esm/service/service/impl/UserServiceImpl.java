package com.epam.esm.service.service.impl;

import com.epam.esm.persistence.entity.Role;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.persistence.model.specification.FindAllSpecification;
import com.epam.esm.persistence.model.specification.FindUserByEmailSpecification;
import com.epam.esm.persistence.model.specification.FindUserByUsernameSpecification;
import com.epam.esm.persistence.repository.RoleRepository;
import com.epam.esm.persistence.repository.UserRepository;
import com.epam.esm.service.dto.user.UserInfoDto;
import com.epam.esm.service.dto.user.UserRegistrationModel;
import com.epam.esm.service.exception.DeletedRoleException;
import com.epam.esm.service.exception.EntityAlreadyExistsException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.mapper.UserMapper;
import com.epam.esm.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String NOT_FOUND_BY_ID = "user with id = %s not found";
    private static final String NOT_FOUND_BY_NAME = "user with name = %s not found";
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public UserInfoDto getById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_BY_ID, id)));
        return mapper.map(user);
    }

    @Override
    public Page<UserInfoDto> getAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(new FindAllSpecification<>(), pageable);
        return userPage.map(mapper::map);
    }

    @Override
    public Optional<UserInfoDto> findByUsername(String username) {
        UserInfoDto result = mapper.map(userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_BY_NAME, username))));
        return Optional.of(result);
    }

    @Override
    public UserInfoDto registerUser(UserRegistrationModel userDto) {

        FindUserByUsernameSpecification usernameSpecification = new FindUserByUsernameSpecification(userDto.getUsername());
        FindUserByEmailSpecification emailSpecification = new FindUserByEmailSpecification(userDto.getEmail());
        userRepository.findAll(usernameSpecification.or(emailSpecification)).stream().findAny().ifPresent(
                (userFound)-> {
                    throw new EntityAlreadyExistsException("user with username " + userDto.getUsername() + " already exists!");
        });
        User user = mapper.map(userDto);
        Optional<Role> optionalRole = roleRepository.findById("USER");
        Role role = optionalRole.orElseThrow(() -> new DeletedRoleException("user cannot be registered with role user"));
        user.setRole(role);
        user.setPassword(encoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return mapper.map(saved);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return DataAccessUtils.singleResult(userRepository.findAll(new FindUserByUsernameSpecification(username)));
    }

}
