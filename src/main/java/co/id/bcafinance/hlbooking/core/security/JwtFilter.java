package co.id.bcafinance.hlbooking.core.security;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:04
@Last Modified 05/05/2024 21:04
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.MyHttpServletRequestWrapper;
import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.configuration.RawHttpServletRequestWrapper;
import co.id.bcafinance.hlbooking.core.Crypto;
import co.id.bcafinance.hlbooking.handler.RequestCapture;
import co.id.bcafinance.hlbooking.handler.XSSAttackExcception;
import co.id.bcafinance.hlbooking.handler.XSSParamException;
import co.id.bcafinance.hlbooking.handler.XSSResponse;
import co.id.bcafinance.hlbooking.service.AuthService;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import co.id.bcafinance.hlbooking.util.XSSValidationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;
    private String [] strExceptionArr = new String[2];
    public JwtFilter() {
        strExceptionArr[0] = "JwtFilter";
    }

    private XSSResponse xssResponse = new XSSResponse();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        authorization = authorization == null ? "": authorization;
        String token = null;
        String username = null;
        MyHttpServletRequestWrapper requestWrapper = null;
        RawHttpServletRequestWrapper rawRequest = null;
        try{

            String strContentType = request.getContentType()==null?"":request.getContentType();
            if(!strContentType.startsWith("multipart/form-data") || "".equals(strContentType)){
                rawRequest = new RawHttpServletRequestWrapper(request);
                requestWrapper = new MyHttpServletRequestWrapper(request, OtherConfig.getKataTerlarang());
                if(!XSSFilteringManual(rawRequest,request,response)){
                    /**
                     * Saat masuk sudah otomatis dari boddy nya error
                     * jadi wrapper yang mentah nya di switch ke object request agar bisa di stop prosesing nya
                     * karena kombinasi hacker untuk memasukkan script bisa di path variable saja atau req param saja , atau body saja
                     * agar program tidak terkecoh oleh kombinasi tersebut , maka sediakan 1 object request mentah nya agar bisa didapat kan script hacker tersebut
                     * dan dimasukkan ke dalam log
                     */
                    request = rawRequest;
                    xssResponse.setStatus(HttpStatus.FORBIDDEN.value());
                    xssResponse.setMessage("XSS attack error");
                    response.getWriter().write(convertObjectToJson(xssResponse));
                    response.setStatus(HttpStatus.FORBIDDEN.value());
//                    strExceptionArr[1] = "doFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) "+ RequestCapture.allRequest(request);
                    LoggingFile.exceptionStringz(strExceptionArr, new XSSAttackExcception("Serangan Hacker"), OtherConfig.getFlagLoging());
                    return;
                }
                request = rawRequest;
            }

            if(!"".equals(authorization) && authorization.startsWith("Bearer ") && authorization.length()>7)
            {
                token = authorization.substring(7);

                token = Crypto.performDecrypt(token);
                username = jwtUtility.getUsernameFromToken(token);
                if(username != null &&
                        SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    if(jwtUtility.validateToken(token)) {
                        String role = jwtUtility.getRoleFromToken(token);
                        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

                        UserDetails userDetails = new User(username, "", authorities);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        catch (XSSParamException e)
        {
            xssResponse.setStatus(HttpStatus.FORBIDDEN.value());
            xssResponse.setMessage("XSS attack error");
            response.getWriter().write(convertObjectToJson(xssResponse));
            response.setStatus(HttpStatus.FORBIDDEN.value());
            strExceptionArr[1] = "doFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) "+ RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
        }
        catch (Exception ex)
        {
            strExceptionArr[1] = "doFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) "+ RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, ex, OtherConfig.getFlagLoging());
        }

        try {
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Boolean XSSFilteringManual(RawHttpServletRequestWrapper requestWrapper,
                                       HttpServletRequest request,
                                       HttpServletResponse response
    )throws IOException,ServletException{
        try {
            String uri = requestWrapper.getRequestURI();
            String decodedURI = URLDecoder.decode(uri, "UTF-8");

            // XSS:  Path Variable Validation
            if (!XSSValidationUtils.isValidURL(decodedURI, OtherConfig.getKataTerlarang())) {
                return false;
            }
            if (!StringUtils.isEmpty(requestWrapper.getBody())) {
                // XSS :  Request Body validation
                if (!XSSValidationUtils.isValidURLPattern(requestWrapper.getBody(), OtherConfig.getKataTerlarang())) {
                    return false;
                }
            }
        } catch (XSSAttackExcception ex) {
            strExceptionArr[1] = "XSSFilteringManual(MyHttpServletRequestWrapper requestWrapper, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)"+ RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, ex, OtherConfig.getFlagLoging());
            return false;
        }  catch (Exception ex) {
            strExceptionArr[1] = "XSSFilteringManual(MyHttpServletRequestWrapper requestWrapper, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)"+ RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, ex, OtherConfig.getFlagLoging());
            return false;
        }

        return true;
    }
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
