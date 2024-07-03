package co.id.bcafinance.hlbooking.service.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/05/2024 09:56
@Last Modified 28/05/2024 09:56
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.KategoriBarangDTO;
import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.handler.RequestCapture;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
//import co.id.bcafinance.hlbooking.model.Lapangan;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import co.id.bcafinance.hlbooking.repo.projectakhir.KategoriBarangRepo;
import co.id.bcafinance.hlbooking.util.GlobalFunction;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class KategoriBarangService {

    @Autowired
    KategoriBarangRepo kategoriBarangRepo;
    @Autowired
    private GlobalFunction globalFunction;
    @Autowired
    private ModulAuthority modulAuthority;

    private String[] strExceptionArr = new String[2];

    private Map<String, Object> mapToken = new HashMap<>();

    public ResponseEntity<Object> create(KategoriBarangDTO kategoriBarangDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        String namaKategori = kategoriBarangDTO.getNamaKategoriBarang();
        if (kategoriBarangRepo.existsByNamaKategoriAndIsActive(namaKategori, true)) {
            return new ResponseHandler().generateResponse("Nama Kategori Barang sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04035", request);
        }

        try {
            KategoriBarang kategoriBarang = new KategoriBarang();
            kategoriBarang.setNamaKategori(kategoriBarangDTO.getNamaKategoriBarang());
            kategoriBarang.setActive(true);
            kategoriBarang.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));

            kategoriBarangRepo.save(kategoriBarang);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHandler().generateResponse("Kategori Barang Gagal Dibuat!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04034", request);
        }

        return new ResponseHandler().generateResponse("Kategori Barang Berhasil Dibuat!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> edit(Long id, KategoriBarangDTO kategoriBarangDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<KategoriBarang> optionalKategoriBarang = kategoriBarangRepo.findById(id);
        if (optionalKategoriBarang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        KategoriBarang k = optionalKategoriBarang.get();

        String namaKategori = kategoriBarangDTO.getNamaKategoriBarang();
        if (!k.getNamaKategori().equals(namaKategori) && kategoriBarangRepo.existsByNamaKategoriAndIsActive(namaKategori, true)) {
            return new ResponseHandler().generateResponse("Nama Kategori Barang sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04036", request);
        }

        try {
            k.setNamaKategori(kategoriBarangDTO.getNamaKategoriBarang());
            k.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            k.setUpdatedAt(new Date());

            kategoriBarangRepo.save(k);
        } catch (Exception e) {
            strExceptionArr[1] = "doEdit(Long id, KategoriBarangDTO kategoriBarangDTO, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Kategori Barang Gagal Diubah !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04023", request);
        }
        return new ResponseHandler().generateResponse("Data Kategori Barang Berhasil Diubah!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<KategoriBarang> optionalKategoriBarang = kategoriBarangRepo.findById(id);
        if (optionalKategoriBarang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        KategoriBarang k = optionalKategoriBarang.get();

        try {
            k.setActive(false);
            k.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            k.setUpdatedAt(new Date());

            kategoriBarangRepo.save(k);

        } catch (Exception e) {
            strExceptionArr[1] = "doDelete(Long id, HttpServletRequest request) - KategoriBarang" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Kategori Barang Gagal Dihapus !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04025", request);
        }
        return new ResponseHandler().generateResponse("Data Kategori Barang Berhasil Dihapus!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> getAllKategori(HttpServletRequest request) {
        try {
            List<String> kategori = kategoriBarangRepo.findAllByIsActiveOrderByNamaKategoriAsc(true).stream()
                    .map(KategoriBarang::getNamaKategori)
                    .collect(Collectors.toList());
            return new ResponseHandler().generateResponse("Data Kategori Barang Berhasil Ditemukan!!",
                    HttpStatus.OK,
                    kategori,
                    null,
                    request);
        } catch (Exception e) {
            return new ResponseHandler().generateResponse("Data Kategori Barang Gagal Ditemukan!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04026",
                    request);
        }
    }

}

