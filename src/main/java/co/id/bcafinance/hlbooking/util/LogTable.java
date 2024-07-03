package co.id.bcafinance.hlbooking.util;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:54
@Last Modified 05/05/2024 21:54
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.handler.RequestCapture;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LogTable {
    private String [] strExceptionArr = new String[2];
    public LogTable() {
        strExceptionArr[0] = "LogTable";
    }
    public void inputLogRequest(String[] datax, Exception exc, String flag, Long createdBy, String urlApi, HttpServletRequest request)
    {
        if(flag.equalsIgnoreCase("y"))
        {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Executors.newSingleThreadExecutor().
                                submit(new ExecutionLogTable(datax, exc, OtherConfig.getFlagLogTable(), createdBy, urlApi)).
                                get(Long.parseLong(OtherConfig.getTimeOutExternalApi()), TimeUnit.SECONDS);
                    } catch (Exception ee) {
                        System.out.println("Nama Thread di LogTable" + Thread.currentThread().getName());
                        strExceptionArr[1] = "inputLogRequest(String[] datax, Exception exc, String flag, Long createdBy, String urlApi, HttpServletRequest request) LINE 126 ---> Thread Problem " + RequestCapture.allRequest(request);
                        LoggingFile.exceptionStringz(strExceptionArr, ee, OtherConfig.getFlagLoging());
                    }
                }
            });
            t.start();
            System.out.println("Thread Log Table " + t.getName());
        }
    }
}

