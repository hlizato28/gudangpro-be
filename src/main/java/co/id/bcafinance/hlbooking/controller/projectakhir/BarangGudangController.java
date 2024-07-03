package co.id.bcafinance.hlbooking.controller.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 24/06/2024 08:20
@Last Modified 24/06/2024 08:20
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.UserDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangGudangDTO;
import co.id.bcafinance.hlbooking.service.projectakhir.BarangGudangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/barang-gudang")
public class BarangGudangController {
    @Autowired
    BarangGudangService barangGudangService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody BarangGudangDTO barangGudangDTO,
                                         HttpServletRequest request) {
        return barangGudangService.create(barangGudangDTO, request);
    }

    @GetMapping("/approved")
    public ResponseEntity<Page<BarangGudangDTO>> getAllApprovedItem(
            @RequestParam(defaultValue = "") String kategori,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BarangGudangDTO> barangGudangDTOPage = barangGudangService.findAll(true, false, kategori, pageable, searchTerm, request);
        return ResponseEntity.ok(barangGudangDTOPage);
    }

    @GetMapping("/not-approved")
    public ResponseEntity<Page<BarangGudangDTO>> getAllNotApprovedItem(
            @RequestParam(defaultValue = "") String kategori,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BarangGudangDTO> barangGudangDTOPage = barangGudangService.findAll(false, false, kategori, pageable, searchTerm, request);
        return ResponseEntity.ok(barangGudangDTOPage);
    }

    @GetMapping("/delete-tba")
    public ResponseEntity<Page<BarangGudangDTO>> getAllDeleteTBA(
            @RequestParam(defaultValue = "") String kategori,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BarangGudangDTO> barangGudangDTOPage = barangGudangService.findAll(true, true, kategori, pageable, searchTerm, request);
        return ResponseEntity.ok(barangGudangDTOPage);
    }

    @PutMapping("/{id}/approve-create")
    public ResponseEntity<Object> approveCreate(@PathVariable("id") Long id,
                                                HttpServletRequest request) {
        return barangGudangService.approveCreate(id, true,request);
    }

    @PutMapping("/{id}/not-approve-create")
    public ResponseEntity<Object> notApproveCreate(@PathVariable("id") Long id,
                                                HttpServletRequest request) {
        return barangGudangService.approveCreate(id, false,request);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id,
                                         HttpServletRequest request) {
        return barangGudangService.delete(id, request);
    }

    @PutMapping("/{id}/approve-delete")
    public ResponseEntity<Object> approveDelete(@PathVariable("id") Long id,
                                                HttpServletRequest request) {
        return barangGudangService.approveDelete(id, true,request);
    }

    @PutMapping("/{id}/not-approve-delete")
    public ResponseEntity<Object> notApproveDelete(@PathVariable("id") Long id,
                                                HttpServletRequest request) {
        return barangGudangService.approveDelete(id, false,request);
    }

    @GetMapping("/list-by-kategori")
    public ResponseEntity<Object> getListByCabangAndKategoriAndJumlah(@RequestParam String kategori,
                                                                      HttpServletRequest request) {
        return barangGudangService.getListByCabangAndKategoriAndJumlah(kategori, request);
    }

    @GetMapping("/data/{nama}")
    public ResponseEntity<Object> getCurrentUser(@PathVariable("nama") String nama,
                                                 HttpServletRequest request){
        return barangGudangService.findByNamaBarang(nama, request);
    }

//    @GetMapping("/all/ca={ca}/da={da}/kat={kategori}")
//    public ResponseEntity<Page<BarangGudangDTO>> getAllBarang(
//            @PathVariable(value = "kategori", required = false) String kategori,
//            @PathVariable(value = "ca", required = false) String ca,
//            @PathVariable(value = "da", required = false) String da,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "") String searchTerm,
//            HttpServletRequest request) {
//
//        if (kategori != null && kategori.trim().isEmpty()) {
//            kategori = null;
//        }
//        if (ca != null && ca.trim().isEmpty()) {
//            ca = null;
//        }
//        if (da != null && da.trim().isEmpty()) {
//            da = null;
//        }
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<BarangGudangDTO> barangGudangDTO = barangGudangService.findAllBarang(kategori, ca, da, pageable, searchTerm, request);
//        return ResponseEntity.ok(barangGudangDTO);
//    }
}

