package co.id.bcafinance.hlbooking.controller.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/05/2024 14:57
@Last Modified 28/05/2024 14:57
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangCabangDTO;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangCabang;
import co.id.bcafinance.hlbooking.service.projectakhir.BarangCabangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/barcb")
public class BarangCabangController {
    @Autowired
    BarangCabangService barangCabangService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody BarangCabangDTO barangCabangDTO,
                                         HttpServletRequest request) {
        return barangCabangService.create(barangCabangDTO, request);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> edit(@PathVariable("id") Long id,
                                       @Valid @RequestBody BarangCabangDTO barangCabangDTO,
                                       HttpServletRequest request) {
        return barangCabangService.edit(id, barangCabangDTO, request);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id,
                                         HttpServletRequest request) {
        return barangCabangService.delete(id, request);
    }

    @GetMapping("/cab={cabang}")
    public ResponseEntity<Page<BarangCabangDTO>> findByCabang(
            @PathVariable("cabang") String cabang,
            @RequestParam(defaultValue = "") String kategori,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BarangCabangDTO> barangCabangDTOPage = barangCabangService.findByCabang(cabang, kategori, searchTerm, pageable, request);
        return ResponseEntity.ok(barangCabangDTOPage);
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<Object> findById( @PathVariable(value = "id") Long id,
                                            HttpServletRequest request){
        return barangCabangService.findById(id,request);
    }

    @PostMapping("/create/all-cabang")
    public ResponseEntity<Object> createForAllCabang(@RequestBody Map<String, String> requestBody,
                                                     HttpServletRequest request) {
        String namaBarang = requestBody.get("namaBarang");
        return barangCabangService.createForAllCabang(namaBarang, request);
    }

    @GetMapping("/list-by-kategori")
    public ResponseEntity<Object> getListByCabangAndKategori(@RequestParam String kategori,
                                                             HttpServletRequest request) {
        return barangCabangService.getListByCabangAndKategori(kategori, request);
    }
}

