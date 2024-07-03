package co.id.bcafinance.hlbooking.service.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 27/05/2024 10:04
@Last Modified 27/05/2024 10:04
Version 1.0
*/


import co.id.bcafinance.hlbooking.configuration.OtherConfig;
//import co.id.bcafinance.hlbooking.dto.LapanganDTO;
import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangDTO;
import co.id.bcafinance.hlbooking.handler.RequestCapture;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
//import co.id.bcafinance.hlbooking.model.Lapangan;
import co.id.bcafinance.hlbooking.model.projectakhir.Unit;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import co.id.bcafinance.hlbooking.repo.projectakhir.BarangRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.KategoriBarangRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.UnitRepo;
import co.id.bcafinance.hlbooking.util.GlobalFunction;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class BarangService {

    @Autowired
    BarangRepo barangRepo;

    @Autowired
    KategoriBarangRepo kategoriBarangRepo;

    @Autowired
    UnitRepo unitRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModulAuthority modulAuthority;

    @Autowired
    private GlobalFunction globalFunction;

    private Map<String, Object> mapToken = new HashMap<>();

    private String[] strExceptionArr = new String[2];

    public ResponseEntity<Object> create(BarangDTO barangDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

//        String cabangUser = mapToken.get("cg").toString();
//        System.out.println("Cabang: " + cabangUser);
//
//        Map<String, Object> unitMap = (Map<String, Object>) mapToken.get("ut");
//        String unitUser = (String) unitMap.get("namaUnit");
//        System.out.println("Unit: " + unitUser);

        String namaBarang = barangDTO.getNamaBarang();
        String kode = barangDTO.getKodeBarang();
        if (barangRepo.existsByNamaBarang(namaBarang) || barangRepo.existsByKodeBarang(kode)) {
            return new ResponseHandler().generateResponse("Kode atau Nama Barang sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04035", request);
        }

        Optional<KategoriBarang> optionalKategori = kategoriBarangRepo.findByNamaKategori(barangDTO.getKategoriBarang());
        if (optionalKategori.isEmpty()) {
            return new ResponseHandler().generateResponse("Kategori Barang tidak ditemukan!!",
                    HttpStatus.NOT_FOUND,
                    null,
                    "FE04033", request);
        }

        KategoriBarang kategori = optionalKategori.get();

        try {
            Barang barang = new Barang();
            barang.setKodeBarang(barangDTO.getKodeBarang());
            barang.setNamaBarang(barangDTO.getNamaBarang());
            barang.setSatuan(barangDTO.getSatuan());
            barang.setDeskripsi(barangDTO.getDeskripsi());
            barang.setKategoriBarang(kategori);
            barang.setActive(true);
            barang.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));

            barangRepo.save(barang);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHandler().generateResponse("Barang Gagal Dibuat!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04032", request);
        }

        return new ResponseHandler().generateResponse("Barang Berhasil Dibuat!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> edit(Long id, BarangDTO barangDTO, HttpServletRequest request) {
        System.out.println("Mencari BarangCabang untuk cabang: " + barangDTO.getKategoriBarang());
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Barang> optionalBarang = barangRepo.findById(id);
        if (optionalBarang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Barang b = optionalBarang.get();

        String namaBarang = barangDTO.getNamaBarang();
        String kode = barangDTO.getKodeBarang();
        if (!b.getNamaBarang().equals(namaBarang) && barangRepo.existsByNamaBarang(namaBarang) ||
                !b.getKodeBarang().equals(kode) && barangRepo.existsByKodeBarang(kode)) {
            return new ResponseHandler().generateResponse("Kode atau Nama Barang sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04036", request);
        }

        Optional<KategoriBarang> optionalKategori = kategoriBarangRepo.findByNamaKategori(barangDTO.getKategoriBarang());
        if (optionalKategori.isEmpty()) {
            System.out.println("Kategori tidak ditemukan untuk: {}"+ barangDTO.getKategoriBarang());
            return globalFunction.dataNotFoundById(request);
        }

        KategoriBarang k = optionalKategori.get();

        System.out.println("Kategori ditemukan: {}" + k);

        try {
            b.setKodeBarang(barangDTO.getKodeBarang());
            b.setNamaBarang(barangDTO.getNamaBarang());
            b.setSatuan(barangDTO.getSatuan());
            b.setDeskripsi(barangDTO.getDeskripsi());
            b.setKategoriBarang(k);
            b.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            b.setUpdatedAt(new Date());

            barangRepo.save(b);
        } catch (Exception e) {
            strExceptionArr[1] = "doEdit(Long id, BarangDTO barangDTO, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Barang Gagal Diubah !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04022", request);
        }
        return new ResponseHandler().generateResponse("Data Barang Berhasil Diubah!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Barang> optionalBarang = barangRepo.findById(id);
        if (optionalBarang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Barang b = optionalBarang.get();

        try {
            b.setActive(false);
            b.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            b.setUpdatedAt(new Date());

            barangRepo.save(b);
        } catch (Exception e) {
            strExceptionArr[1] = "doDelete(Long id, HttpServletRequest request) - Barang" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Barang Gagal Dihapus !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04024", request);
        }
        return new ResponseHandler().generateResponse("Data Barang Berhasil Dihapus!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public Page<BarangDTO> findAllBarang(String kategori, Pageable pageable, String searchTerm, HttpServletRequest request) {
        Page<Barang> barangPage;

        KategoriBarang k = null;

        if (kategori != null && !kategori.trim().isEmpty()) {
            Optional<KategoriBarang> optionalKategori = kategoriBarangRepo.findByNamaKategori(kategori);
            if (optionalKategori.isEmpty()) {
                return Page.empty();
            }

            k = optionalKategori.get();
        }
        barangPage = barangRepo.findByNamaBarangOrKodeBarangAndIsActiveContainingIgnoreCase(k,true, searchTerm, pageable);

        return barangPage.map(barang -> modelMapper.map(barang, BarangDTO.class));
    }

    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<Barang> optionalBarang = barangRepo.findById(id);
        if (optionalBarang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Barang b = optionalBarang.get();

        BarangDTO barangDTO = modelMapper.map(b, BarangDTO.class);

        return new ResponseHandler().generateResponse("Data Ditemukan!!",
                HttpStatus.OK,
                barangDTO,
                null, request);
    }

    public ResponseEntity<Object> findByNamaBarang(String nama, HttpServletRequest request) {
        Optional<Barang> optionalBarang = barangRepo.findByNamaBarang(nama);
        if (optionalBarang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Barang b = optionalBarang.get();

        BarangDTO barangDTO = modelMapper.map(b, BarangDTO.class);

        return new ResponseHandler().generateResponse("Data Ditemukan!!",
                HttpStatus.OK,
                barangDTO,
                null, request);
    }

    public ResponseEntity<Object> getListByKategori(String kategori, HttpServletRequest request) {
        try {
            Optional<KategoriBarang> optionalKategori = kategoriBarangRepo.findByNamaKategori(kategori);
            if (optionalKategori.isEmpty()) {
                return globalFunction.dataNotFoundById(request);
            }

            KategoriBarang k = optionalKategori.get();

            List<String> barang = barangRepo.findAllByKategoriBarangAndIsActiveOrderByNamaBarangAsc(k,true).stream()
                    .map(Barang::getNamaBarang)
                    .collect(Collectors.toList());
            return new ResponseHandler().generateResponse("Data Barang Berhasil Ditemukan!!",
                    HttpStatus.OK,
                    barang,
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

