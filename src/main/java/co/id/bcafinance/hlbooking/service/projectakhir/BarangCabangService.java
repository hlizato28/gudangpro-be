package co.id.bcafinance.hlbooking.service.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/05/2024 14:36
@Last Modified 28/05/2024 14:36
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangCabangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangDTO;
import co.id.bcafinance.hlbooking.handler.RequestCapture;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.projectakhir.Cabang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangCabang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import co.id.bcafinance.hlbooking.repo.projectakhir.BarangCabangRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.BarangRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.CabangRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.KategoriBarangRepo;
import co.id.bcafinance.hlbooking.util.GlobalFunction;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BarangCabangService {
    @Autowired
    BarangCabangRepo barangCabangRepo;

    @Autowired
    CabangService cabangService;

    @Autowired
    KategoriBarangRepo kategoriBarangRepo;

    @Autowired
    BarangRepo barangRepo;

    @Autowired
    private ModulAuthority modulAuthority;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GlobalFunction globalFunction;

    private Map<String, Object> mapToken = new HashMap<>();

    private String[] strExceptionArr = new String[2];

    public ResponseEntity<Object> create(BarangCabangDTO barangCabangDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Barang> optionalBarang = barangRepo.findByNamaBarang(barangCabangDTO.getNamaBarang());
        if (optionalBarang.isEmpty()) {
            return new ResponseHandler().generateResponse("Barang tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }

        Barang barang = optionalBarang.get();
        String cabang = barangCabangDTO.getCabang();
        if (barangCabangRepo.existsByBarangAndCabangAndIsActive(barang, cabang, true)) {
            return new ResponseHandler().generateResponse("Item sudah terdaftar di cabang ini! ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    null,
                    request);
        }

        try {
            BarangCabang barangCabang = new BarangCabang();
            barangCabang.setBarang(barang);
            barangCabang.setCabang(cabang);
            barangCabang.setActive(true);
            barangCabang.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));

            barangCabangRepo.save(barangCabang);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHandler().generateResponse("Barang Cabang Gagal Dibuat!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04032", request);
        }

        return new ResponseHandler().generateResponse("Barang Cabang Berhasil Dibuat!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> edit(Long id, BarangCabangDTO barangCabangDTO, HttpServletRequest request) {

        Optional<BarangCabang> optionalBarangCabang = barangCabangRepo.findById(id);
        if (optionalBarangCabang.isEmpty()) {
            return new ResponseHandler().generateResponse("Item tidak ditemukan pada cabang ini!",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }
        BarangCabang barangCabang = optionalBarangCabang.get();

        Optional<Barang> optionalBarang = barangRepo.findByNamaBarang(barangCabangDTO.getNamaBarang());
        if (optionalBarang.isEmpty()) {
            return new ResponseHandler().generateResponse("Barang tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }

        Barang barang = optionalBarang.get();
        String cabang = barangCabangDTO.getCabang();
        if (barangCabangRepo.existsByBarangAndCabangAndIsActive(barang, cabang, true)) {
            return new ResponseHandler().generateResponse("Item sudah terdaftar di cabang ini! ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    null,
                    request);
        }

        try {
            barangCabang.setBarang(barang);
            barangCabang.setCabang(cabang);
            barangCabang.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            barangCabang.setUpdatedAt(new Date());

            barangCabangRepo.save(barangCabang);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHandler().generateResponse("Barang Cabang Gagal Diubah!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04032", request);
        }

        return new ResponseHandler().generateResponse("Barang Cabang Berhasil Diubah!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {

        Optional<BarangCabang> optionalBarangCabang = barangCabangRepo.findById(id);
        if (optionalBarangCabang.isEmpty()) {
            return new ResponseHandler().generateResponse("Item tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }
        BarangCabang barangCabang = optionalBarangCabang.get();

        try {
            barangCabang.setActive(false);
            barangCabang.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            barangCabang.setUpdatedAt(new Date());

            barangCabangRepo.save(barangCabang);

            return new ResponseHandler().generateResponse("Item berhasil di hapus!",
                    HttpStatus.OK,
                    null,
                    null,
                    request);
        } catch (Exception e) {
            return new ResponseHandler().generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null, null, request);
        }
    }

    public Page<BarangCabangDTO> findByCabang(String cabang, String kat, String searchTerm, Pageable pageable, HttpServletRequest request) {
        Page<BarangCabang> barangCabangPage;

        KategoriBarang k = null;

        if (kat != null && !kat.trim().isEmpty()) {
            Optional<KategoriBarang> optionalKategori = kategoriBarangRepo.findByNamaKategori(kat);
            if (optionalKategori.isEmpty()) {
                return Page.empty();
            }

            k = optionalKategori.get();
        }
        barangCabangPage = barangCabangRepo.findByCabangAndKategoriBarangAndSearch(cabang, k, true, searchTerm, pageable);

        return barangCabangPage.map(barangCabang -> {
            BarangCabangDTO dto = new BarangCabangDTO();
            dto.setIdBarangCabang(barangCabang.getIdBarangCabang());
            dto.setNamaBarang(barangCabang.getBarang().getNamaBarang());
            dto.setCabang(barangCabang.getCabang());
            dto.setKategoriBarang(barangCabang.getBarang().getKategoriBarang().getNamaKategori());
            dto.setKodeBarang(barangCabang.getBarang().getKodeBarang());
            return dto;
        });
    }

    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<BarangCabang> optionalBarangCabang = barangCabangRepo.findById(id);
        if (optionalBarangCabang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        BarangCabang bc = optionalBarangCabang.get();

        return new ResponseHandler().generateResponse("Data Ditemukan!!",
                HttpStatus.OK,
                bc,
                null, request);
    }

    public ResponseEntity<Object> createForAllCabang(String namaBarang, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Barang> optionalBarang = barangRepo.findByNamaBarang(namaBarang);
        if (optionalBarang.isEmpty()) {
            return new ResponseHandler().generateResponse("Barang tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }

        Barang barang = optionalBarang.get();

        try {
            // Mendapatkan semua cabang
            ResponseEntity<Object> cabangResponse = cabangService.getAllCabang(request);
            if (cabangResponse.getStatusCode() != HttpStatus.OK) {
                return cabangResponse; // Mengembalikan response error jika gagal mendapatkan cabang
            }

            // Ekstrak list cabang dari response
            Map<String, Object> responseBody = (Map<String, Object>) cabangResponse.getBody();
            List<String> cabangList = (List<String>) responseBody.get("data");

            List<String> successfulCabang = new ArrayList<>();
            List<String> existingCabang = new ArrayList<>();

            for (String cabang : cabangList) {
                if (!barangCabangRepo.existsByBarangAndCabangAndIsActive(barang, cabang, true)) {
                    BarangCabang barangCabang = new BarangCabang();
                    barangCabang.setBarang(barang);
                    barangCabang.setCabang(cabang);
                    barangCabang.setActive(true);
                    barangCabang.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));

                    barangCabangRepo.save(barangCabang);
                    successfulCabang.add(cabang);
                } else {
                    existingCabang.add(cabang);
                }
            }

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("successfulCabang", successfulCabang);
            resultMap.put("existingCabang", existingCabang);

            if (successfulCabang.isEmpty() && !existingCabang.isEmpty()) {
                return new ResponseHandler().generateResponse("Barang sudah ada di semua cabang",
                        HttpStatus.BAD_REQUEST,
                        resultMap,
                        "FE04034", request);
            }

            return new ResponseHandler().generateResponse("Barang berhasil dibuat untuk semua cabang",
                    HttpStatus.OK,
                    resultMap,
                    null, request);

        } catch (Exception e) {
            strExceptionArr[1] = "createForAllCabang(String namaBarang, HttpServletRequest request) - Barang" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return new ResponseHandler().generateResponse("Barang Cabang Gagal Dibuat!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04032", request);
        }
    }

    public ResponseEntity<Object> getListByCabangAndKategori(String kategori, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<KategoriBarang> optionalKategoriBarang = kategoriBarangRepo.findByNamaKategori(kategori);
        if (optionalKategoriBarang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }
        KategoriBarang k = optionalKategoriBarang.get();

        String cabangUser = mapToken.get("cg").toString();

        try {
            List<String> barangCabang = barangCabangRepo.listByCabangAndKategori(true, k,cabangUser).stream()
                    .map(bc -> bc.getBarang().getNamaBarang())
                    .collect(Collectors.toList());
            return new ResponseHandler().generateResponse("Data Barang Berhasil Ditemukan!!",
                    HttpStatus.OK,
                    barangCabang,
                    null,
                    request);
        } catch (Exception e) {
            return new ResponseHandler().generateResponse("Data Barang Gagal Ditemukan!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04026",
                    request);
        }
    }

}

