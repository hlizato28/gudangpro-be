package co.id.bcafinance.hlbooking.service;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 27/06/2024 08:41
@Last Modified 27/06/2024 08:41
Version 1.0
*/

import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangGudangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.DetailBalancingDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.pengajuan.DetailPengajuanGudangCabangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.pengajuan.PengajuanGudangCabangDTO;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Balancing;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangGudang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.DetailBalancing;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import co.id.bcafinance.hlbooking.model.projectakhir.pengajuan.cabang.DetailPengajuanGudangCabang;
import co.id.bcafinance.hlbooking.model.projectakhir.pengajuan.cabang.PengajuanGudangCabang;
import co.id.bcafinance.hlbooking.repo.UserRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.*;
import co.id.bcafinance.hlbooking.service.projectakhir.BalancingService;
import co.id.bcafinance.hlbooking.util.GlobalFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PengajuanGudangCabangService {
    @Autowired
    PengajuanGudangCabangRepo pengajuanGudangCabangRepo;
    @Autowired
    DetailPengajuanGudangCabangRepo detailPengajuanGudangCabangRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    BalancingRepo balancingRepo;
    @Autowired
    BarangGudangRepo barangGudangRepo;
    @Autowired
    BalancingService balancingService;
    @Autowired
    DetailBalancingRepo detailBalancingRepo;
    @Autowired
    private ModulAuthority modulAuthority;

    @Autowired
    private GlobalFunction globalFunction;

    private Map<String, Object> mapToken = new HashMap<>();
    private String[] strExceptionArr = new String[2];


    public ResponseEntity<Object> create(PengajuanGudangCabangDTO pengajuanDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);
        String cabang = mapToken.get("cg").toString();

        Optional<User> optionalUser = userRepo.findById(Long.parseLong(mapToken.get("de").toString()));
        if (optionalUser.isEmpty()) {
            return new ResponseHandler().generateResponse("User tidak ditemukan!",
                    HttpStatus.NOT_FOUND,
                    null,
                    "FE04033", request);
        }

        User user = optionalUser.get();

        List<String> errorMessages = new ArrayList<>();

        for (DetailPengajuanGudangCabangDTO detailDTO : pengajuanDTO.getDetails()) {
            Optional<BarangGudang> optionalBarang = barangGudangRepo.findByNamaBarang(true, detailDTO.getNamaBarang(), cabang, true, false);
            if (optionalBarang.isPresent()) {
                BarangGudang barangGudang = optionalBarang.get();
                if (barangGudang.getJumlah() < detailDTO.getJumlahDiminta()) {
                    errorMessages.add(String.format("Stok untuk barang '%s' tidak mencukupi!",
                            barangGudang.getBarangCabang().getBarang().getNamaBarang()));
                }
            } else {
                errorMessages.add(String.format("Barang dengan nama '%s' tidak ditemukan", detailDTO.getNamaBarang()));
            }
        }

        if (!errorMessages.isEmpty()) {
            return new ResponseHandler().generateResponse(
                    "Pengajuan tidak dapat diproses: " + String.join(", ", errorMessages),
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04032", request);
        }

        try {
            PengajuanGudangCabang pengajuan = new PengajuanGudangCabang();
            pengajuan.setUser(user);
            pengajuan.setActive(true);
            pengajuan.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));

            PengajuanGudangCabang savedPengajuan = pengajuanGudangCabangRepo.save(pengajuan);

            for (DetailPengajuanGudangCabangDTO detailDTO : pengajuanDTO.getDetails()) {
                Optional<BarangGudang> optionalBarangGudang = barangGudangRepo.findByNamaBarang(true, detailDTO.getNamaBarang(), cabang, true, false);
                if (optionalBarangGudang.isEmpty()) {
                    return new ResponseHandler().generateResponse("Barang tidak ditemukan!!",
                            HttpStatus.NOT_FOUND,
                            null,
                            "FE04033", request);
                }
                BarangGudang barangGudang = optionalBarangGudang.get();

                if (barangGudang.getJumlah() < detailDTO.getJumlahDiminta()) {
                    return new ResponseHandler().generateResponse("Stok barang tidak mencukupi!!",
                            HttpStatus.NOT_FOUND,
                            null,
                            "FE04033", request);
                }

                DetailPengajuanGudangCabang detail = new DetailPengajuanGudangCabang();
                detail.setPengajuanGudangCabang(savedPengajuan);
                detail.setBarangGudang(barangGudang);
                detail.setJumlahDiminta(detailDTO.getJumlahDiminta());
                detail.setJumlahDiterima(0L);
                detail.setJumlahApproved(0L);
                detail.setApproved(false);
                detail.setActive(true);
                detail.setDiterima(false);
                detail.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));
                detailPengajuanGudangCabangRepo.save(detail);
            }

            return new ResponseHandler().generateResponse("Pengajuan Gudang Berhasil Dibuat!",
                    HttpStatus.OK,
                    null,
                    null, request);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHandler().generateResponse("Pengajuan Gudang Gagal Dibuat!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04032", request);
        }
    }

    public Page<PengajuanGudangCabangDTO> getPengajuanDetailByCabang(HttpServletRequest request, Pageable pageable) {
        mapToken = modulAuthority.checkAuthorization(request);
        String cabang = mapToken.get("cg").toString();

        Page<PengajuanGudangCabang> pengajuanPage = pengajuanGudangCabangRepo.findPengajuanDetailsByCabangAndNotApproved(cabang, true, false, pageable);

        return pengajuanPage.map(pengajuan -> {
            PengajuanGudangCabangDTO dto = mapPengajuanToDTO(pengajuan);

            List<DetailPengajuanGudangCabang> details = detailPengajuanGudangCabangRepo.findByPengajuanGudangCabangAndIsApprovedAndIsActive(pengajuan, false, true);
            List<DetailPengajuanGudangCabangDTO> detailDTOs = details.stream()
                    .map(this::mapDetailToDTO)
                    .collect(Collectors.toList());

            dto.setDetails(detailDTOs);
            return dto;
        });
    }

    public Page<DetailPengajuanGudangCabangDTO> getPengajuanDetailByUser(HttpServletRequest request, Pageable pageable) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<User> optionalUser = userRepo.findById(Long.parseLong(mapToken.get("de").toString()));
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan!");
        }

        User user = optionalUser.get();

        try {
            List<PengajuanGudangCabang> pengajuans = pengajuanGudangCabangRepo.findPengajuanDetailsByUserAndApproved(user, true, true, false);

            if (pengajuans.isEmpty()) {
                return new PageImpl<>(Collections.emptyList(), pageable, 0);
            }

            List<DetailPengajuanGudangCabang> allDetails = new ArrayList<>();
            for (PengajuanGudangCabang pengajuan : pengajuans) {
                List<DetailPengajuanGudangCabang> details = detailPengajuanGudangCabangRepo.findByPengajuanGudangCabangAndIsApprovedAndIsDiterimaAndIsActive(pengajuan, true, false, true);
                allDetails.addAll(details);
            }

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allDetails.size());
            List<DetailPengajuanGudangCabang> pageContent = allDetails.subList(start, end);

            List<DetailPengajuanGudangCabangDTO> detailDTOs = pageContent.stream()
                    .map(this::mapDetailToDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(detailDTOs, pageable, allDetails.size());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal mengambil data pengajuan");
        }
    }

    public ResponseEntity<Object> approval(DetailPengajuanGudangCabangDTO detailPengajuanGudangCabangDTO, Boolean app, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<DetailPengajuanGudangCabang> optionalDetailPengajuanGudangCabang = detailPengajuanGudangCabangRepo.findById(detailPengajuanGudangCabangDTO.getIdDetailPengajuanGudangCabang());
        if (optionalDetailPengajuanGudangCabang.isEmpty()) {
            return new ResponseHandler().generateResponse("Item tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }
        DetailPengajuanGudangCabang detailPengajuanGudangCabang = optionalDetailPengajuanGudangCabang.get();

        BarangGudang barangGudang = detailPengajuanGudangCabang.getBarangGudang();

        try {
            if (app) {
                balancingService.pergerakanBarang(barangGudang.getIdBarangGudang(), detailPengajuanGudangCabangDTO.getJumlahApproved(), 0L, 0L, request);

                detailPengajuanGudangCabang.setApproved(true);
                detailPengajuanGudangCabang.setJumlahApproved(detailPengajuanGudangCabangDTO.getJumlahApproved());
                barangGudang.setJumlah(barangGudang.getJumlah() + detailPengajuanGudangCabangDTO.getJumlahDiminta() - detailPengajuanGudangCabangDTO.getJumlahApproved());
                detailPengajuanGudangCabang.setJumlahDiterima(0L);
            } else {
                detailPengajuanGudangCabang.setActive(false);
                barangGudang.setJumlah(barangGudang.getJumlah() + detailPengajuanGudangCabangDTO.getJumlahDiminta());
            }
            detailPengajuanGudangCabang.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            detailPengajuanGudangCabang.setUpdatedAt(new Date());

            detailPengajuanGudangCabangRepo.save(detailPengajuanGudangCabang);
            barangGudangRepo.save(barangGudang);

            return new ResponseHandler().generateResponse("Item berhasil di approve!",
                    HttpStatus.OK,
                    null,
                    null,
                    request);
        } catch (Exception e) {
            return new ResponseHandler().generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null, request);
        }
    }

    public ResponseEntity<Object> diterima(DetailPengajuanGudangCabangDTO detailPengajuanGudangCabangDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<DetailPengajuanGudangCabang> optionalDetailPengajuanGudangCabang = detailPengajuanGudangCabangRepo.findById(detailPengajuanGudangCabangDTO.getIdDetailPengajuanGudangCabang());
        if (optionalDetailPengajuanGudangCabang.isEmpty()) {
            return new ResponseHandler().generateResponse("Item tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }
        DetailPengajuanGudangCabang detailPengajuanGudangCabang = optionalDetailPengajuanGudangCabang.get();

        BarangGudang barangGudang = detailPengajuanGudangCabang.getBarangGudang();

        try {
            detailPengajuanGudangCabang.setJumlahDiterima(detailPengajuanGudangCabangDTO.getJumlahDiterima());
            detailPengajuanGudangCabang.setDiterima(true);
            detailPengajuanGudangCabang.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            detailPengajuanGudangCabang.setUpdatedAt(new Date());

            detailPengajuanGudangCabangRepo.save(detailPengajuanGudangCabang);

            barangGudang.setJumlah(barangGudang.getJumlah() + detailPengajuanGudangCabang.getJumlahApproved());
            barangGudangRepo.save(barangGudang);

            balancingService.pergerakanBarang(barangGudang.getIdBarangGudang(), 0L, detailPengajuanGudangCabangDTO.getJumlahDiterima(), 0L, request);
            
            return new ResponseHandler().generateResponse("Item sudah di terima!",
                    HttpStatus.OK,
                    null,
                    null,
                    request);
        } catch (Exception e) {
            return new ResponseHandler().generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null, request);
        }
    }

    public Page<PengajuanGudangCabangDTO> findRevisiPengajuan(Long idBarangGudang, Date tgl, HttpServletRequest request, Pageable pageable) {

        Optional<BarangGudang> optionalBarangGudang = barangGudangRepo.findById(idBarangGudang);
        if (optionalBarangGudang.isEmpty()) {
            return Page.empty();
        }
        BarangGudang barangGudang = optionalBarangGudang.get();

        Page<PengajuanGudangCabang> pengajuanPage = pengajuanGudangCabangRepo.findPengajuanDetailsByBarangGudangAndTanggal(barangGudang, tgl, pageable);

        return pengajuanPage.map(pengajuan -> {
            PengajuanGudangCabangDTO dto = mapPengajuanToDTO(pengajuan);

            List<DetailPengajuanGudangCabang> details = detailPengajuanGudangCabangRepo.findByPengajuanGudangCabangAndIsApprovedAndIsActive(pengajuan, true, true);
            List<DetailPengajuanGudangCabangDTO> detailDTOs = details.stream()
                    .map(this::mapDetailToDTO)
                    .collect(Collectors.toList());

            dto.setDetails(detailDTOs);
            return dto;
        });
    }

    public ResponseEntity<Object> revisiDetailPengajuan(Long idDetailBalancing, List<PengajuanGudangCabangDTO> pengajuanGudangCabangDTOList, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        List<DetailPengajuanGudangCabang> updatedDetails = new ArrayList<>();
        Long totalJumlahDiterima = 0L;

        for (PengajuanGudangCabangDTO pengajuan : pengajuanGudangCabangDTOList) {
            for (DetailPengajuanGudangCabangDTO detail : pengajuan.getDetails()) {
                Optional<DetailPengajuanGudangCabang> optionalDetail = detailPengajuanGudangCabangRepo.findById(detail.getIdDetailPengajuanGudangCabang());

                if (optionalDetail.isPresent()) {
                    DetailPengajuanGudangCabang revisiDetail = optionalDetail.get();
                    revisiDetail.setJumlahApproved(detail.getJumlahApproved());
                    revisiDetail.setJumlahDiterima(detail.getJumlahDiterima());
                    revisiDetail.setDiterima(false);
                    revisiDetail.setUpdatedAt(new Date());
                    revisiDetail.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
                    updatedDetails.add(revisiDetail);
                    totalJumlahDiterima += detail.getJumlahDiterima();
                } else {
                    return ResponseEntity.badRequest().body("Detail dengan ID " + detail.getIdDetailPengajuanGudangCabang() + " tidak ditemukan.");
                }
            }
        }

        detailPengajuanGudangCabangRepo.saveAll(updatedDetails);

        //Edit Detail Balancing
        Optional<DetailBalancing> optionalDetailBalancing = detailBalancingRepo.findById(idDetailBalancing);

        DetailBalancing detailBalancing = optionalDetailBalancing.get();
        detailBalancing.setStokAkhir(detailBalancing.getStokAwal() - totalJumlahDiterima);
        detailBalancing.setUpdatedAt(new Date());
        detailBalancing.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));

        detailBalancingRepo.save(detailBalancing);

       //Edit Barang Gudang
        BarangGudang barangGudang = detailBalancing.getBarangGudang();
        barangGudang.setJumlah(detailBalancing.getStokAwal() - totalJumlahDiterima);
        barangGudang.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
        barangGudang.setUpdatedAt(new Date());

        barangGudangRepo.save(barangGudang);

        return ResponseEntity.ok("Revisi bulk berhasil dilakukan dan total jumlah diterima telah diupdate");
    }

    private PengajuanGudangCabangDTO mapPengajuanToDTO(PengajuanGudangCabang pengajuan) {
        PengajuanGudangCabangDTO dto = new PengajuanGudangCabangDTO();
        dto.setIdPengajuanGudangCabang(pengajuan.getIdPengajuanGudangCabang());
        dto.setUser(pengajuan.getUser().getNama());
        dto.setUnit(pengajuan.getUser().getUnit().getNamaUnit());
        return dto;
    }

    private DetailPengajuanGudangCabangDTO mapDetailToDTO(DetailPengajuanGudangCabang detail) {
        DetailPengajuanGudangCabangDTO detailDto = new DetailPengajuanGudangCabangDTO();
        detailDto.setIdDetailPengajuanGudangCabang(detail.getIdDetailPengajuanGudangCabang());
        detailDto.setKodeBarang(detail.getBarangGudang().getBarangCabang().getBarang().getKodeBarang());
        detailDto.setNamaBarang(detail.getBarangGudang().getBarangCabang().getBarang().getNamaBarang());
        detailDto.setSatuan(detail.getBarangGudang().getBarangCabang().getBarang().getSatuan());
        detailDto.setJumlahDiminta(detail.getJumlahDiminta());
        detailDto.setStok(detail.getBarangGudang().getJumlah());
        detailDto.setJumlahApproved(detail.getJumlahApproved());
        detailDto.setJumlahDiterima(detail.getJumlahDiterima());
        detailDto.setIdBarangGudang(detail.getBarangGudang().getIdBarangGudang());
        return detailDto;
    }
}

