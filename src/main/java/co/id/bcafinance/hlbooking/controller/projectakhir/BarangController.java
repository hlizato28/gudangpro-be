package co.id.bcafinance.hlbooking.controller.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/05/2024 10:07
@Last Modified 28/05/2024 10:07
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.DetailBalancingDTO;
import co.id.bcafinance.hlbooking.service.projectakhir.BalancingService;
import co.id.bcafinance.hlbooking.service.projectakhir.BarangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/barang")
public class BarangController {
    @Autowired
    BarangService barangService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody BarangDTO barangDTO,
                                               HttpServletRequest request) {
        return barangService.create(barangDTO, request);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> edit(@PathVariable("id") Long id,
                                       @Valid @RequestBody BarangDTO barangDTO,
                                       HttpServletRequest request) {
        return barangService.edit(id, barangDTO, request);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id,
                                         HttpServletRequest request) {
        return barangService.delete(id, request);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BarangDTO>> getAllBarang(
            @RequestParam(defaultValue = "") String kategori,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BarangDTO> barangDTOPage = barangService.findAllBarang(kategori, pageable, searchTerm, request);
        return ResponseEntity.ok(barangDTOPage);
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<Object> findById( @PathVariable(value = "id") Long id,
                                                    HttpServletRequest request){
        return barangService.findById(id,request);
    }

    @GetMapping("/nama/{nama}")
    public ResponseEntity<Object> findByNama( @PathVariable(value = "nama") String nama,
                                            HttpServletRequest request){
        return barangService.findByNamaBarang(nama,request);
    }

    @GetMapping("/list/{kategori}")
    public ResponseEntity<Object> getListByKategori(@PathVariable(value = "kategori") String kategori,
                                                    HttpServletRequest request) {
        return barangService.getListByKategori(kategori,request);
    }

}

