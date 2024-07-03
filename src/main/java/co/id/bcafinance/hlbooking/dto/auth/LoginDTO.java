package co.id.bcafinance.hlbooking.dto.auth;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:11
@Last Modified 05/05/2024 21:11
Version 1.0
*/

import co.id.bcafinance.hlbooking.util.ConstantMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LoginDTO {
    @NotNull(message = ConstantMessages.USERNAME_NULL)
    @NotEmpty(message = ConstantMessages.USERNAME_EMPTY)
    @NotBlank(message = ConstantMessages.USERNAME_BLANK)
    private String username;

    @NotNull(message = ConstantMessages.PASSWORD_NULL)
    @NotEmpty(message = ConstantMessages.PASSWORD_EMPTY)
    @NotBlank(message = ConstantMessages.PASSWORD_BLANK)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

