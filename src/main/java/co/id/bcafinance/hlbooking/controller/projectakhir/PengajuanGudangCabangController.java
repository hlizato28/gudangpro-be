package co.id.bcafinance.hlbooking.controller.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 27/06/2024 11:22
@Last Modified 27/06/2024 11:22
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.projectakhir.barang.ReportDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.pengajuan.DetailPengajuanGudangCabangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.pengajuan.PengajuanGudangCabangDTO;
import co.id.bcafinance.hlbooking.service.PengajuanGudangCabangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/pengajuan/cabang")
public class PengajuanGudangCabangController {
    @Autowired
    PengajuanGudangCabangService pengajuanGudangCabangService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody PengajuanGudangCabangDTO pengajuanGudangCabangDTO,
                                         HttpServletRequest request) {
        return pengajuanGudangCabangService.create(pengajuanGudangCabangDTO, request);
    }

    @GetMapping("/detail-by-cabang")
    public ResponseEntity<Page<PengajuanGudangCabangDTO>> getDetailPengajuanByCabang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PengajuanGudangCabangDTO> pengajuanGudangCabangDTOPage = pengajuanGudangCabangService.getPengajuanDetailByCabang(request, pageable);
        return ResponseEntity.ok(pengajuanGudangCabangDTOPage);
    }

    @GetMapping("/detail-by-user")
    public ResponseEntity<Page<DetailPengajuanGudangCabangDTO>> getDetailPengajuanByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DetailPengajuanGudangCabangDTO> detailPengajuanDTOPage = pengajuanGudangCabangService.getPengajuanDetailByUser(request, pageable);
        return ResponseEntity.ok(detailPengajuanDTOPage);
    }

    @PutMapping("/approve")
    public ResponseEntity<Object> approve(@Valid @RequestBody DetailPengajuanGudangCabangDTO detailPengajuanGudangCabangDTO,
                                          HttpServletRequest request) {
        return pengajuanGudangCabangService.approval(detailPengajuanGudangCabangDTO, true,request);
    }

    @PutMapping("/not-approve")
    public ResponseEntity<Object> notApprove(@Valid @RequestBody DetailPengajuanGudangCabangDTO detailPengajuanGudangCabangDTO,
                                             HttpServletRequest request) {
        return pengajuanGudangCabangService.approval(detailPengajuanGudangCabangDTO, false,request);
    }

    @PutMapping("/diterima")
    public ResponseEntity<Object> diterima(@Valid @RequestBody DetailPengajuanGudangCabangDTO detailPengajuanGudangCabangDTO,
                                             HttpServletRequest request) {
        return pengajuanGudangCabangService.diterima(detailPengajuanGudangCabangDTO, request);
    }

    @GetMapping("/revisi-out/{id}")
    public ResponseEntity<Page<PengajuanGudangCabangDTO>> revisiPengajuan(
            @PathVariable(value = "id") Long id,
            @RequestParam Long tgl,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Date tglFormat = new Date(tgl);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(tglFormat);

        try {
            tglFormat = sdf.parse(formattedDate);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<PengajuanGudangCabangDTO> pengajuanGudangCabangDTOPage = pengajuanGudangCabangService.findRevisiPengajuan(id, tglFormat, request, pageable);
        return ResponseEntity.ok(pengajuanGudangCabangDTOPage);
    }

    @PutMapping("/revisi-out/save/{id}")
    public ResponseEntity<Object> revisiDetailPengajuan(
            @PathVariable Long id,
            @RequestBody List<PengajuanGudangCabangDTO> pengajuanGudangCabangDTOList,
            HttpServletRequest request) {
        return pengajuanGudangCabangService.revisiDetailPengajuan(id, pengajuanGudangCabangDTOList, request);
    }
}

