package co.id.bcafinance.hlbooking.service.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/06/2024 11:29
@Last Modified 28/06/2024 11:29
Version 1.0
*/

import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.*;
import co.id.bcafinance.hlbooking.dto.projectakhir.pengajuan.DetailPengajuanGudangCabangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.pengajuan.PengajuanGudangCabangDTO;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.*;
import co.id.bcafinance.hlbooking.model.projectakhir.pengajuan.cabang.DetailPengajuanGudangCabang;
import co.id.bcafinance.hlbooking.repo.projectakhir.*;
import co.id.bcafinance.hlbooking.util.GlobalFunction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BalancingService {
    @Autowired
    DetailBalancingRepo detailBalancingRepo;

    @Autowired
    BalancingRepo balancingRepo;

    @Autowired
    KategoriBarangRepo kategoriBarangRepo;

    @Autowired
    DetailPengajuanGudangCabangRepo detailPengajuanGudangCabangRepo;
    @Autowired
    BarangGudangRepo barangGudangRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModulAuthority modulAuthority;

    @Autowired
    private GlobalFunction globalFunction;

    private Map<String, Object> mapToken = new HashMap<>();

    private String[] strExceptionArr = new String[2];

    public ResponseEntity<Object> pergerakanBarang(Long id, Long app, Long out, Long in, HttpServletRequest request) {
        Optional<BarangGudang> optionalBarangGudang = barangGudangRepo.findById(id);
        if (optionalBarangGudang.isEmpty()) {
            return new ResponseHandler().generateResponse("Barang tidak ditemukan!!",
                    HttpStatus.NOT_FOUND,
                    null,
                    "FE04033", request);
        }
        BarangGudang barangGudang = optionalBarangGudang.get();

        Optional<DetailBalancing> optionalDetailBalancing = detailBalancingRepo.findByBarangGudangAndBalancingAndIsActive(barangGudang, null, true);

        DetailBalancing detailBalancing;
        if (optionalDetailBalancing.isPresent()) {
            detailBalancing = optionalDetailBalancing.get();
        } else {
            Optional<DetailBalancing> optionalLastDetailBalancing = detailBalancingRepo.findTopLatestByBarangGudangAndIsActive(barangGudang, true);

            DetailBalancing lastDetailBalancing = optionalLastDetailBalancing.orElse(null);

            detailBalancing = new DetailBalancing();
            detailBalancing.setBarangGudang(barangGudang);
            detailBalancing.setStokAwal(lastDetailBalancing != null ? lastDetailBalancing.getStokAkhir() : (barangGudang.getJumlah() + app));
            detailBalancing.setStokAkhir(0L);
            detailBalancing.setBarangIn(0L);
            detailBalancing.setBarangOut(0L);
            detailBalancing.setActive(true);
        }

        detailBalancing.setBarangIn(detailBalancing.getBarangIn() + in);
        detailBalancing.setBarangOut(detailBalancing.getBarangOut() + out);
        detailBalancing.setUpdatedAt(new Date());

        if (app > 0) {
            if (detailBalancing.getStokAkhir() == 0L) {
                detailBalancing.setStokAkhir(detailBalancing.getStokAwal() - app);
            } else {
                detailBalancing.setStokAkhir(detailBalancing.getStokAkhir() - app);
            }
        } else {
            detailBalancing.setStokAkhir(detailBalancing.getStokAwal() + detailBalancing.getBarangIn() - detailBalancing.getBarangOut());
        }

        detailBalancingRepo.save(detailBalancing);

        // Update stok di BarangGudang
        barangGudang.setJumlah(detailBalancing.getStokAkhir());
        barangGudangRepo.save(barangGudang);

        return new ResponseHandler().generateResponse("Pergerakan barang berhasil dicatat",
                HttpStatus.OK,
                null,
                null, request);
    }

    public Page<DetailBalancingDTO> getAllNoBalancing(String kat, Date tanggal, Pageable pageable, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);
        String cabang = mapToken.get("cg").toString();

        Optional<KategoriBarang> optionalKategoriBarang = kategoriBarangRepo.findByNamaKategori(kat);
        if (optionalKategoriBarang.isEmpty()) {
            return Page.empty();
        }
        KategoriBarang kategoriBarang = optionalKategoriBarang.get();

        try {
            boolean balancingExists = detailBalancingRepo.existByKategoriBarangAndTanggal(cabang, kategoriBarang, tanggal);

            if (balancingExists) {
                throw new RuntimeException("Balancing untuk kategori " + kategoriBarang.getNamaKategori() + " sudah dibuat untuk hari ini.");
            }

            Page<Object[]> combinedResults = detailBalancingRepo.findBarangGudangAndDetailBalancing(cabang, kategoriBarang, tanggal, pageable);

            return combinedResults.map(result -> {
                DetailBalancingDTO dto = new DetailBalancingDTO();
                BarangGudang barangGudang = (BarangGudang) result[0];
                DetailBalancing detailBalancing = (DetailBalancing) result[1];

                dto.setKodeBarang(barangGudang.getBarangCabang().getBarang().getKodeBarang());
                dto.setNamaBarang(barangGudang.getBarangCabang().getBarang().getNamaBarang());
                dto.setSatuan(barangGudang.getBarangCabang().getBarang().getSatuan());
                dto.setIdBarangGudang(barangGudang.getIdBarangGudang());

                if (detailBalancing != null) {
                    dto.setIdDetailBalancing(detailBalancing.getIdDetailBalancing());
                    dto.setStokAwal(detailBalancing.getStokAwal());
                    dto.setBarangIn(detailBalancing.getBarangIn());
                    dto.setBarangOut(detailBalancing.getBarangOut());
                    dto.setStokAkhir(detailBalancing.getStokAkhir());
                } else {
                    dto.setIdDetailBalancing(null);
                    dto.setStokAwal(barangGudang.getJumlah());
                    dto.setBarangIn(0L);
                    dto.setBarangOut(0L);
                    dto.setStokAkhir(barangGudang.getJumlah());
                }

                return dto;
            });

        } catch (Exception e) {
            System.out.println(e);
            return Page.empty();
        }
    }

    public ResponseEntity<Object> balancing(BalancingDTO balancingDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);
        String cabang = mapToken.get("cg").toString();
        Long userId = Long.parseLong(mapToken.get("de").toString());

        try {
            Balancing balancing = new Balancing();
            balancing.setApproved(false);
            balancing.setRevisi(false);
            balancing.setActive(true);
            balancing.setCreatedBy(userId);
            balancing.setCreatedAt(new Date());

            balancingRepo.save(balancing);

            for (DetailBalancingDTO dto : balancingDTO.getDetails()) {
                Optional<BarangGudang> optionalBarangGudang = barangGudangRepo.findByBarangCabang_Barang_KodeBarangAndBarangCabang_Cabang(dto.getKodeBarang(), cabang);
                if (optionalBarangGudang.isEmpty()) {
                    return new ResponseHandler().generateResponse("Barang tidak ditemukan!!",
                            HttpStatus.NOT_FOUND,
                            null,
                            "FE04034", request);
                }
                BarangGudang barangGudang = optionalBarangGudang.get();

                Optional<DetailBalancing> optionalDetailBalancing = detailBalancingRepo.findByBarangGudangAndBalancingAndIsActive(barangGudang, null, true);
                DetailBalancing detailBalancing;
                if (optionalDetailBalancing.isPresent()) {
                    detailBalancing = optionalDetailBalancing.get();
                } else {
                    detailBalancing = new DetailBalancing();
                }

                detailBalancing.setBarangGudang(barangGudang);
                detailBalancing.setBalancing(balancing);
                detailBalancing.setStokAwal(dto.getStokAwal());
                detailBalancing.setBarangIn(dto.getBarangIn());
                detailBalancing.setBarangOut(dto.getBarangOut());
                detailBalancing.setStokAkhir(dto.getStokAkhir());
                detailBalancing.setActive(true);

                if (detailBalancing.getIdDetailBalancing() == null) {
                    detailBalancing.setCreatedBy(userId);
                } else {
                    detailBalancing.setUpdatedBy(userId);
                    detailBalancing.setUpdatedAt(new Date());
                }

                detailBalancingRepo.save(detailBalancing);
            }

            return new ResponseHandler().generateResponse("Balancing Berhasil Dibuat!",
                    HttpStatus.OK,
                    null,
                    null, request);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHandler().generateResponse("Balancing Gagal Dibuat!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04032", request);
        }
    }

    public Page<ReportDTO> getReportBalancing(String kat, Date tanggal, Boolean app, Pageable pageable, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);
        String cabang = mapToken.get("cg").toString();

        Optional<KategoriBarang> optionalKategoriBarang = kategoriBarangRepo.findByNamaKategori(kat);
        if (optionalKategoriBarang.isEmpty()) {
            return Page.empty();
        }
        KategoriBarang kategoriBarang = optionalKategoriBarang.get();

        Page<DetailBalancing> detailBalancingPage = detailBalancingRepo.findFilteredDetailBalancing(cabang, app, kategoriBarang, tanggal, pageable);

        List<Long> idBarangGudangList = detailBalancingPage.getContent().stream()
                .map(db -> db.getBarangGudang().getIdBarangGudang())
                .collect(Collectors.toList());

        List<Object[]> pengajuanData = detailBalancingRepo.findPengajuanDataByBarangGudangIds(idBarangGudangList, tanggal);

        Map<Long, Map<String, Long>> pengajuanMap = new HashMap<>();
        for (Object[] data : pengajuanData) {
            Long idBarangGudang = (Long) data[0];
            String namaUnitGroup = (String) data[1];
            Long jumlahDiterima = (Long) data[2];

            pengajuanMap.computeIfAbsent(idBarangGudang, k -> new HashMap<>()).put(namaUnitGroup, jumlahDiterima);
        }

        return new PageImpl<>(
                Collections.singletonList(createReportDTO(detailBalancingPage.getContent(), pengajuanMap)),
                pageable,
                detailBalancingPage.getTotalElements()
        );
    }

    public ResponseEntity<Object> approveBalancing(ReportDTO reportDTO, Boolean isApp, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        try {
            if (reportDTO.getDetails().isEmpty()) {
                return new ResponseHandler().generateResponse("Data balancing kosong",
                        HttpStatus.BAD_REQUEST,
                        null,
                        null,
                        request);
            }

            Long idDetailBalancing = reportDTO.getDetails().get(0).getIdDetailBalancing();

            Optional<DetailBalancing> optionalDetailBalancing = detailBalancingRepo.findById(idDetailBalancing);
            if (optionalDetailBalancing.isEmpty()) {
                return new ResponseHandler().generateResponse("Detail balancing tidak ditemukan",
                        HttpStatus.NOT_FOUND,
                        null,
                        null,
                        request);
            }

            Balancing balancing = optionalDetailBalancing.get().getBalancing();

            KategoriBarang kategoriBarang = optionalDetailBalancing.get().getBarangGudang().getBarangCabang().getBarang().getKategoriBarang();

            if (isApp) {
                balancing.setApproved(true);
                balancing.setRevisi(false);
                balancing.setUpdatedAt(new Date());
                balancing.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            } else {
                balancing.setApproved(false);
                balancing.setRevisi(true);
                balancing.setUpdatedAt(new Date());
                balancing.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            }

            balancingRepo.save(balancing);

            return new ResponseHandler().generateResponse("Balancing berhasil diapprove",
                    HttpStatus.OK,
                    null,
                    null,
                    request);
        } catch (Exception e) {
            return new ResponseHandler().generateResponse(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    null,
                    request);
        }
    }

    public Page<BalancingDTO> getRevisi(Pageable pageable, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);
        String cabang = mapToken.get("cg").toString();

        Page<Balancing> balancingPage = balancingRepo.findByIsRevisiAndIsActiveAndCabang(cabang, pageable);

        return balancingPage.map(balancing -> {
            BalancingDTO dto = new BalancingDTO();
            dto.setIdBalancing(balancing.getIdBalancing());
            dto.setCreatedAt(balancing.getCreatedAt());

            Optional<DetailBalancing> optionalDetailBalancing = detailBalancingRepo.findTop1ByBalancing(balancing);
            dto.setKategori(optionalDetailBalancing.get().getBarangGudang().getBarangCabang().getBarang().getKategoriBarang().getNamaKategori());

            return dto;
        });
    }

    public Page<DetailBalancingDTO> findAllByBalancingId(Long id, Pageable pageable, HttpServletRequest request) {

        Optional<Balancing> optionalBalancing = balancingRepo.findById(id);
        if (optionalBalancing.isEmpty()) {
            return Page.empty();
        }

        Balancing b = optionalBalancing.get();

        Page<DetailBalancing> detailBalancingPage = detailBalancingRepo.findByBalancingAndIsActive(b, true, pageable);

        return detailBalancingPage.map(db -> {
            DetailBalancingDTO dto = modelMapper.map(db, DetailBalancingDTO.class);

            dto.setSatuan(db.getBarangGudang().getBarangCabang().getBarang().getSatuan());

            return dto;
        });
    }

    public ResponseEntity<Object> revisiBalancing(Long idBalancing, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Balancing> optionalBalancing = balancingRepo.findById(idBalancing);
        if (optionalBalancing.isEmpty()) {
            return new ResponseHandler().generateResponse("Balancing tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }

        Balancing balancing = optionalBalancing.get();
        balancing.setRevisi(false);
        balancing.setApproved(false);
        balancing.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
        balancing.setUpdatedAt(new Date());

        balancingRepo.save(balancing);

        return ResponseEntity.ok("Revisi bulk berhasil dilakukan dan total jumlah diterima telah diupdate");
    }


    private ReportBalancingDTO createDetailReportDTO(DetailBalancing db, Map<Long, Map<String, Long>> pengajuanMap) {
        ReportBalancingDTO dto = new ReportBalancingDTO();
        dto.setIdDetailBalancing(db.getIdDetailBalancing());
        dto.setKodeBarang(db.getBarangGudang().getBarangCabang().getBarang().getKodeBarang());
        dto.setNamaBarang(db.getBarangGudang().getBarangCabang().getBarang().getNamaBarang());
        dto.setStokAwal(db.getStokAwal());
        dto.setStokAhkhir(db.getStokAkhir());
        dto.setBarangIn(db.getBarangIn());
        dto.setBarangOut(db.getBarangOut());

        Long idBarangGudang = db.getBarangGudang().getIdBarangGudang();
        Map<String, Long> unitCounts = pengajuanMap.getOrDefault(idBarangGudang, new HashMap<>());

        dto.setOpr(unitCounts.getOrDefault("Operation", 0L));
        dto.setUc(unitCounts.getOrDefault("CS Used", 0L));
        dto.setNc(unitCounts.getOrDefault("CS New", 0L));
        dto.setKkb(unitCounts.getOrDefault("KKB", 0L));
        dto.setDs(unitCounts.getOrDefault("DS", 0L));
        dto.setAsr(unitCounts.getOrDefault("ASR", 0L));

        return dto;
    }

    private ReportDTO createReportDTO(List<DetailBalancing> detailBalancings, Map<Long, Map<String, Long>> pengajuanMap) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setDetails(detailBalancings.stream()
                .map(detailBalancing -> createDetailReportDTO(detailBalancing, pengajuanMap))
                .collect(Collectors.toList()));
        return reportDTO;
    }


}

