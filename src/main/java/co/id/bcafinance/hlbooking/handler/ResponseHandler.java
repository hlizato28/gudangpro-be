package co.id.bcafinance.hlbooking.handler;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:39
@Last Modified 05/05/2024 21:39
Version 1.0
*/

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public ResponseHandler() {
    }

    public ResponseEntity<Object> generateResponse(String message,
                                                   HttpStatus status,
                                                   Object responseObj,
                                                   Object errorCode,
                                                   HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj==null?"":responseObj);
        map.put("timestamp", new Date());
        map.put("success",!status.isError());

        if (responseObj instanceof Page) {
            Page<?> page = (Page<?>) responseObj;
            Map<String, Object> pageMap = new HashMap<>();
            pageMap.put("content", page.getContent());
            pageMap.put("totalElements", page.getTotalElements());
            pageMap.put("totalPages", page.getTotalPages());
            pageMap.put("currentPage", page.getNumber());
            pageMap.put("size", page.getSize());
            map.put("data", pageMap);
        }

        if(errorCode != null)
        {
            map.put("errorCode",errorCode);
            map.put("path",request.getPathInfo());
        }
        return new ResponseEntity<Object>(map,status);
    }
}

