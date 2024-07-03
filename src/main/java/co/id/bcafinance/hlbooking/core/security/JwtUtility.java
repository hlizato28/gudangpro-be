package co.id.bcafinance.hlbooking.core.security;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:05
@Last Modified 05/05/2024 21:05
Version 1.0
*/

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {
    private static final long serialVersionUID = 234234523523L;
    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60;//untuk t menit 5 * 60 * 1000
    @Value("${jwt.secret}")
    private String secretKey;
    private String strArrAuth [] = null;

    public Map<String,Object> getApiAuthorization(String token,
                                                  Map<String,Object> mapz){
        Claims claims = getAllClaimsFromToken(token);

        mapz.put("de",claims.get("de"));
        mapz.put("sub",claims.getSubject());//untuk subject / username sudah ada di claims token JWT
        mapz.put("cg",claims.get("cg"));
        mapz.put("ut",claims.get("ut"));
        mapz.put("re",claims.get("re"));
        mapz.put("nu",claims.get("nu"));

        return mapz;
    }

    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("re", String.class));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    //parameter token habis waktu nya
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token untuk user
    public String generateToken(UserDetails userDetails, Map<String,Object> claims) {
        claims = (claims==null)?new HashMap<String,Object>():claims;
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public Boolean validateToken(String token) {
        String username = getUsernameFromToken(token);
        return (username!=null && !isTokenExpired(token));
    }
}

