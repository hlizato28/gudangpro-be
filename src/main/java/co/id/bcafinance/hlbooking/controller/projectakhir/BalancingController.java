package co.id.bcafinance.hlbooking.controller.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 30/06/2024 21:15
@Last Modified 30/06/2024 21:15
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.projectakhir.barang.*;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.DetailBalancing;
import co.id.bcafinance.hlbooking.service.projectakhir.BalancingService;
import co.id.bcafinance.hlbooking.service.projectakhir.BarangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/balancing")
public class BalancingController {
    @Autowired
    BalancingService balancingService;

    @Autowired
    BarangService barangService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<DetailBalancingDTO>> getAllBalancing(
            @RequestParam(defaultValue = "") String kategori,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date tanggal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DetailBalancingDTO> detailBalancingDTOPage = balancingService.getBalancing(kategori, tanggal, pageable, request);
        return ResponseEntity.ok(detailBalancingDTOPage);
    }

    @GetMapping("/report/no-approve")
    public ResponseEntity<Page<ReportDTO>> getReportBalancingNoApprove(
            @RequestParam(defaultValue = "") String kategori,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date tanggal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReportDTO> reportDTOPage = balancingService.getReportBalancing(kategori, tanggal,false, false, pageable, request);
        return ResponseEntity.ok(reportDTOPage);
    }

    @GetMapping("/report/with-approve")
    public ResponseEntity<Page<ReportDTO>> getReportBalancingWithApprove(
            @RequestParam(defaultValue = "") String kategori,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date tanggal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReportDTO> reportDTOPage = balancingService.getReportBalancing(kategori, tanggal,true, false, pageable, request);
        return ResponseEntity.ok(reportDTOPage);
    }

    @PostMapping("/balanced")
    public ResponseEntity<Object> balancing(@RequestBody @Valid BalancingDTO balancingDTO,
                                            HttpServletRequest request) {
        return balancingService.balancing(balancingDTO, request);
    }

    @PutMapping("/to/approve")
    public ResponseEntity<Object> approveBalancing(@RequestBody @Valid ReportDTO reportDTO,
                                                   HttpServletRequest request) {
        return balancingService.approveBalancing(reportDTO, true, request);
    }

    @PutMapping("/to/revisi")
    public ResponseEntity<Object> revisiBalancing(@RequestBody @Valid ReportDTO reportDTO,
                                                  HttpServletRequest request) {
        return balancingService.approveBalancing(reportDTO, false, request);
    }


}

