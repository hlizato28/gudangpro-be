package co.id.bcafinance.hlbooking.configuration;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 20:33
@Last Modified 05/05/2024 20:33
Version 1.0
*/

import co.id.bcafinance.hlbooking.core.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:smtpconfig.properties")
public class SMTPConfig {
    private static String emailUserName;
    private static String emailPassword;
    private static String emailHost;
    private static String emailPort;
    private static String emailPortSSL;
    private static String emailPortTLS;
    private static String emailAuth;
    private static String emailStartTLSEnable;
    private static String emailSMTPSocketFactoryClass;
    private static String emailSMTPTimeout;

    public static String getEmailSMTPTimeout() {
        return emailSMTPTimeout;
    }

    @Value("${email.smtp.timeout}")
    private void setEmailSMTPTimeout(String emailSMTPTimeout) {
        SMTPConfig.emailSMTPTimeout = emailSMTPTimeout;
    }

    @Value("${email.username}")
    private void setEmailUserName(String emailUserName) {
        this.emailUserName = emailUserName;
    }

    @Value("${email.password}")
    private void setEmailPassword(String emailPassword) {
        this.emailPassword = Crypto.performDecrypt(emailPassword);
    }

    @Value("${email.host}")
    private void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    @Value("${email.port}")
    private void setEmailPort(String emailPort) {
        this.emailPort = emailPort;
    }

    @Value("${email.port.ssl}")
    private void setEmailPortSSL(String emailPortSSL) {
        this.emailPortSSL = emailPortSSL;
    }

    @Value("${email.port.tls}")
    private void setEmailPortTLS(String emailPortTLS) {
        this.emailPortTLS = emailPortTLS;
    }

    @Value("${email.auth}")
    private void setEmailAuth(String emailAuth) {
        this.emailAuth = emailAuth;
    }

    @Value("${email.starttls.enable}")
    private void setEmailStartTLSEnable(String emailStartTLSEnable) {
        this.emailStartTLSEnable = emailStartTLSEnable;
    }
    @Value("${email.smtp.socket.factory.class}")
    private void setEmailSMTPSocketFactoryClass(String emailSMTPSocketFactoryClass) {
        this.emailSMTPSocketFactoryClass = emailSMTPSocketFactoryClass;
    }

    public static String getEmailUserName() {
        return emailUserName;
    }

    public static String getEmailPassword() {
        return emailPassword;
    }

    public static String getEmailHost() {
        return emailHost;
    }

    public static String getEmailPort() {
        return emailPort;
    }

    public static String getEmailPortSSL() {
        return emailPortSSL;
    }

    public static String getEmailPortTLS() {
        return emailPortTLS;
    }

    public static String getEmailAuth() {
        return emailAuth;
    }

    public static String getEmailStartTLSEnable() {
        return emailStartTLSEnable;
    }

    public static String getEmailSMTPSocketFactoryClass() {
        return emailSMTPSocketFactoryClass;
    }

    @Bean
    public  static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}



