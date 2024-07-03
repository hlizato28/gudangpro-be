package co.id.bcafinance.hlbooking.configuration;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 20:30
@Last Modified 05/05/2024 20:30
Version 1.0
*/

import co.id.bcafinance.hlbooking.handler.XSSParamException;
import co.id.bcafinance.hlbooking.handler.XSSResponse;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import co.id.bcafinance.hlbooking.util.XSSValidationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.List;

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final String body;
    private List<String> listKataTerlarang;
    private String [] strExceptionArr = {"MyHttpServletRequestWrapper",""};
    public MyHttpServletRequestWrapper(HttpServletRequest request, List<String> listKataTerlarang) {
        /** agar request dikembalikan ke semula*/
        super(request);
        this.listKataTerlarang = listKataTerlarang;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();

            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                char[] charBuffer = new char[128];
                int bytesRead = -1;

                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
//            System.out.println("Error reading the request body...");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
//                    System.out.println("Error closing bufferedReader...");
                }
            }
        }
        body = stringBuilder.toString();
    }

    private boolean filteringz(String input) {
//        System.out.println("param:" + input);
        XSSResponse errorResponse = new XSSResponse();
        if (!XSSValidationUtils.isValidURL(input, listKataTerlarang)) {

            errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
            errorResponse.setMessage("Anda diketahui Berniat Jahat !!");
            try {
                String response = XSSValidationUtils.convertObjectToJson(errorResponse);
                strExceptionArr[1] = "filteringz(String input) -> Value : Request Param ("+input+")";
                LoggingFile.exceptionStringz(strExceptionArr, new XSSParamException("Serangan Hacker"), OtherConfig.getFlagLoging());
                throw new XSSParamException(response);
            }
            catch (JsonProcessingException e) {

                return false;
            }
        }
        return true;
    }

    /** XSS :  VALIDASI QUERY PARAM */
    @Override
    public String getParameter(String paramName) {
        String value = super.getParameter(paramName);
        filteringz(value);
        return value;
    }

    /** XSS :  VALIDASI QUERY PARAM */
    @Override
    public String[] getParameterValues(String paramName) {
        String values[] = super.getParameterValues(paramName);
        if (null != values) {
            for (int index = 0; index < values.length; index++) {
                filteringz(values[index]);
            }
        }
        return values;
    }

    @Override
    public ServletInputStream getInputStream () {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());

        ServletInputStream inputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read () throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return inputStream;
    }

    @Override
    public BufferedReader getReader(){
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        return this.body;
    }
}

