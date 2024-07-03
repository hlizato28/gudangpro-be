package co.id.bcafinance.hlbooking.dto.auth;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:12
@Last Modified 05/05/2024 21:12
Version 1.0
*/

import co.id.bcafinance.hlbooking.util.ConstantMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RegisDTO {

    @NotNull(message = ConstantMessages.USERNAME_NULL)
    @NotEmpty(message = ConstantMessages.USERNAME_EMPTY)
    @NotBlank(message = ConstantMessages.USERNAME_BLANK)
    private String username;


    @NotNull
    @NotEmpty
    @NotBlank
    private String cabang;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
    }
}
