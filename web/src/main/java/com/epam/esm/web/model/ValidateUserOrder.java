package com.epam.esm.web.model;

import com.epam.esm.service.dto.user.UserInfoDto;
import com.epam.esm.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateUserOrder {

    private final UserService service;

    @Autowired
    public ValidateUserOrder(UserService service) {
        this.service = service;
    }

    public boolean validateUserOrder(Authentication authentication, Long userId) {
        Optional<UserInfoDto> dto = service.findByUsername(authentication.getName());
        UserInfoDto userInfoDto = dto.orElse(new UserInfoDto());
        return userId.equals(userInfoDto.getId()) ||
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("read_all_orders"));
    }
}
