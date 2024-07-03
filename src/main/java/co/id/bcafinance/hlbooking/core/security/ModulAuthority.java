package co.id.bcafinance.hlbooking.core.security;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:06
@Last Modified 05/05/2024 21:06
Version 1.0
*/

import co.id.bcafinance.hlbooking.core.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class ModulAuthority {
    @Autowired
    private JwtUtility jwtUtility;
    private String strToken = "";

    public Map<String , Object> checkAuthorization(HttpServletRequest request){
        strToken = request.getHeader("Authorization").substring(7);
        strToken = Crypto.performDecrypt(strToken);
        return jwtUtility.getApiAuthorization(strToken,new HashMap<String,Object>());
    }
}
