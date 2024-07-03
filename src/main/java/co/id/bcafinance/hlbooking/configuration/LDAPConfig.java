package co.id.bcafinance.hlbooking.configuration;


import co.id.bcafinance.hlbooking.core.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ldap.properties")
public class LDAPConfig {
    private static String ldapUrl;
    private static String basicAuthUname;
    private static String basicAuthPw;

    public static String getLdapUrl() {
        return ldapUrl;
    }

    public static String getBasicAuthUname() {
        return basicAuthUname;
    }

    public static String getBasicAuthPw() {
        return Crypto.performDecrypt(basicAuthPw);
    }

    @Value("${ldap.url}")
    private void setLdapUrl(String ldapUrl) {
        LDAPConfig.ldapUrl = ldapUrl;
    }

    @Value("${basic.auth.uname}")
    private void setBasicAuthUname(String basicAuthUname) {
        LDAPConfig.basicAuthUname = basicAuthUname;
    }

    @Value("${basic.auth.pw}")
    private void setBasicAuthPw(String basicAuthPw) {
        LDAPConfig.basicAuthPw = basicAuthPw;
    }

}
