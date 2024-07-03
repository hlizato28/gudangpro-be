package co.id.bcafinance.hlbooking.controller.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/05/2024 10:40
@Last Modified 28/05/2024 10:40
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.projectakhir.barang.KategoriBarangDTO;
import co.id.bcafinance.hlbooking.service.projectakhir.KategoriBarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/katb")
public class KategoriBarangController {

    @Autowired
    private KategoriBarangService kategoriBarangService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody KategoriBarangDTO kategoriBarangDTO,
                                         HttpServletRequest request) {
        return kategoriBarangService.create(kategoriBarangDTO, request);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> edit(@PathVariable("id") Long id,
                                       @Valid @RequestBody KategoriBarangDTO kategoriBarangDTO,
                                       HttpServletRequest request) {
        return kategoriBarangService.edit(id, kategoriBarangDTO, request);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id,
                                         HttpServletRequest request) {
        return kategoriBarangService.delete(id, request);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllKategori(HttpServletRequest request) {
        return kategoriBarangService.getAllKategori(request);
    }
}

