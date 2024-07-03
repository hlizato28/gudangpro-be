package co.id.bcafinance.hlbooking.controller.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/05/2024 13:42
@Last Modified 28/05/2024 13:42
Version 1.0
*/

import co.id.bcafinance.hlbooking.service.projectakhir.CabangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cabang")
public class CabangController {
    @Autowired
    private CabangService cabangService;

    @GetMapping("/list")
    public ResponseEntity<Object> getAllCabang(HttpServletRequest request) {
        return cabangService.getAllCabang(request);
    }
}

