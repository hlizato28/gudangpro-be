package co.id.bcafinance.hlbooking.configuration;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 20:31
@Last Modified 05/05/2024 20:31
Version 1.0
*/

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("classpath:other.properties")
public class OtherConfig {
    private static String flagLoging;
    private static String flagLogTable;
    private static String flagSmtpActive;
    private static String flagPwdTrap;
    private static String maxCounterToken;
    private static String osSplitting;//penambahan 20-12-2023

    private static String howToDownloadReport;//penambahan 07-01-2024
    private static String pathSeparatorReport;
    /** baiknya yg berhubungan dengan directory / path dijadikan environment variables*/
    private static String pathGeneratePDF;

    /** url api untuk input ke log table error */
    private static String urlAPILogRequestError;
    private static String timeOutExternalApi;

    private static List<String> kataTerlarang;//penambahan 09-03-2024 untuk XSS saja

    public static List<String> getKataTerlarang() {
        return kataTerlarang;
    }
    @Value("#{'${kata.terlarang}'.split(',')}")
    private void setKataTerlarang(List<String> kataTerlarang) {
        OtherConfig.kataTerlarang = kataTerlarang;
    }

    public static String getTimeOutExternalApi() {
        return timeOutExternalApi;
    }

    @Value("${time.out.external.api}")
    private void setTimeOutExternalApi(String timeOutExternalApi) {
        OtherConfig.timeOutExternalApi = timeOutExternalApi;
    }

    public static String getUrlAPILogRequestError() {
        return urlAPILogRequestError;
    }
    @Value("${url.api.logrequesterror}")
    private void setUrlAPILogRequestError(String urlAPILogRequestError) {
        OtherConfig.urlAPILogRequestError = urlAPILogRequestError;
    }
    public static String getPathSeparatorReport() {
        return pathSeparatorReport;
    }

    public static String getPathGeneratePDF() {
        return pathGeneratePDF;
    }


    @Value("${path.separator.report}")
    private void setPathSeparatorReport(String pathSeparatorReport) {
        OtherConfig.pathSeparatorReport = pathSeparatorReport;
    }

//    @Value("${path.generate.pdf}")
//    private void setPathGeneratePDF(String pathGeneratePDF) {
//        OtherConfig.pathGeneratePDF = pathGeneratePDF;
//    }

    public static String getHowToDownloadReport() {
        return howToDownloadReport;
    }


    @Value("${how.to.download.report}")
    private void setHowToDownloadReport(String howToDownloadReport) {
        OtherConfig.howToDownloadReport = howToDownloadReport;
    }

    public static String getOsSplitting() {
        return osSplitting;
    }

    @Value("${os.splitting}")
    private void setOsSplitting(String osSplitting) {
        OtherConfig.osSplitting = osSplitting;
    }

    /*
            penambahan 13-12-2023
            Regex untuk split username dan password
            sehingga tidak perlu cek 2x ke database
         */
    private static String flagSplitToken;
    public static String getFlagSplitToken() {
        return flagSplitToken;
    }
    @Value("${flag.split.token}")
    private void setFlagSplitToken(String flagSplitToken) {
        OtherConfig.flagSplitToken = flagSplitToken;
    }
    public static String getMaxCounterToken() {
        return maxCounterToken;
    }
    @Value("${max.counter.token}")
    private void setMaxCounterToken(String maxCounterToken) {
        OtherConfig.maxCounterToken = maxCounterToken;
    }
    public static String getFlagPwdTrap() {
        return flagPwdTrap;
    }
    @Value("${flag.pwd.trap}")
    private void setFlagPwdTrap(String flagPwdTrap) {
        OtherConfig.flagPwdTrap = flagPwdTrap;
    }
    public static String getFlagSmtpActive() {
        return flagSmtpActive;
    }
    @Value("${flag.smtp.active}")
    private void setFlagSmtpActive(String flagSmtpActive) {
        OtherConfig.flagSmtpActive = flagSmtpActive;
    }
    public static String getFlagLogTable() {
        return flagLogTable;
    }
    @Value("${flag.log.table}")
    private void setFlagLogTable(String flagLogTable) {
        OtherConfig.flagLogTable = flagLogTable;
    }
    public static String getFlagLoging() {
        return flagLoging;
    }
    @Value("${flag.loging}")
    private void setFlagLoging(String flagLoging) {
        OtherConfig.flagLoging = flagLoging;
    }
}

