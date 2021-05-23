package com.epam.esm.service.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRegistrationModel {
    @NotBlank(message = "Username cannot be empty or null")
    @Size(max = 100, message = "username cannot be longer than 100 symbols!")
    private String username;
    @NotBlank(message = "email cannot be empty or null")
    @Size(max = 100, message = "email cannot be longer than 100 symbols!")
    @Email
    private String email;
    @Size(min = 6, message = "password must be longer than 6 symbols")
    private String password;

    public UserRegistrationModel() {
    }

    public UserRegistrationModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
