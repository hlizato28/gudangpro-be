package co.id.bcafinance.hlbooking.util;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:52
@Last Modified 05/05/2024 21:52
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.core.SMTPCore;

import java.util.concurrent.Callable;

public class ExecuteSMTP implements Callable<String> {
    private String mailAddress;
    private String subject;
    private String [] strVerification;
    private String pathFile;

    private StringBuilder stringBuilder = new StringBuilder();
    private String [] strException = new String[2];

    public ExecuteSMTP() {
        strException[0] = "ExecuteSMTP";

    }

    public ExecuteSMTP(String mailAddress, String subject, String[] strVerification, String pathFile) {
        this.mailAddress = mailAddress;
        this.subject = subject;
        this.strVerification = strVerification;
        this.pathFile = pathFile;
        strException[0] = "ExecuteSMTP";
    }

    @Override
    public String call() throws Exception {
        sendSMTPToken(mailAddress,subject,strVerification,pathFile);
        return null;
    }

    public Boolean sendSMTPToken(String mailAddress,
                                 String subject,
                                 String [] strVerification,
                                 String pathFile)
    {
        try
        {
            if(OtherConfig.getFlagSmtpActive().equalsIgnoreCase("y") && !mailAddress.equals(""))
            {
                String strContent = new ReadTextFileSB(pathFile).getContentFile();
                strContent = strContent.replace("#JKVM3NH",strVerification[0]);
                strContent = strContent.replace("XF#31NN",strVerification[1]);
                strContent = strContent.replace("8U0_1GH$",strVerification[2]);

                String [] strEmail = {mailAddress};
                SMTPCore sc = new SMTPCore();
                return  sc.sendMailWithAttachment(strEmail,
                        subject,
                        strContent,
                        "SSL",null);
            }
        }
        catch (Exception e)
        {
            strException[1]="sendSMTPToken(String mailAddress, String subject, String purpose, String token) -- LINE 38";
            LoggingFile.exceptionStringz(strException,e, OtherConfig.getFlagLoging());
            return false;
        }
        return true;
    }
}

