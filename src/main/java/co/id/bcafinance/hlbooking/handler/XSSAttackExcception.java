package co.id.bcafinance.hlbooking.handler;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:40
@Last Modified 05/05/2024 21:40
Version 1.0
*/

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class XSSAttackExcception extends RuntimeException{
    public XSSAttackExcception() {
    }

    public XSSAttackExcception(String message) {
        super(message);
    }
}

