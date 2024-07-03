package co.id.bcafinance.hlbooking.service.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 23/06/2024 23:44
@Last Modified 23/06/2024 23:44
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.dto.UserDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangCabangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangGudangDTO;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.Role;
import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.model.projectakhir.Unit;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangCabang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangGudang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import co.id.bcafinance.hlbooking.repo.projectakhir.*;
import co.id.bcafinance.hlbooking.util.GlobalFunction;
import co.id.bcafinance.hlbooking.util.LogTable;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BarangGudangService {
    @Autowired
    BarangGudangRepo barangGudangRepo;
    @Autowired
    UnitRepo unitRepo;
    @Autowired
    BarangRepo barangRepo;
    @Autowired
    BarangCabangRepo barangCabangRepo;
    @Autowired
    KategoriBarangRepo kategoriBarangRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModulAuthority modulAuthority;

    @Autowired
    private GlobalFunction globalFunction;

    private Map<String, Object> mapToken = new HashMap<>();

    private String[] strExceptionArr = new String[2];

    public ResponseEntity<Object> create(BarangGudangDTO barangGudangDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        String cabangUser = mapToken.get("cg").toString();

//        Map<String, Object> unitMap = (Map<String, Object>) mapToken.get("ut");
//        String unitUser = (String) unitMap.get("namaUnit");
//        System.out.println("Unit: " + unitUser);

        Optional<Barang> optionalBarang = barangRepo.findByNamaBarang(barangGudangDTO.getNamaBarang());
        if (optionalBarang.isEmpty()) {
            return new ResponseHandler().generateResponse("Barang tidak ditemukan!!",
                    HttpStatus.NOT_FOUND,
                    null,
                    "FE04033", request);
        }
        Barang barang = optionalBarang.get();

        Optional<BarangCabang> optionalBarangCabang = barangCabangRepo.findByBarangAndCabangAndIsActive(barang, cabangUser, true);
        if (optionalBarangCabang.isEmpty()) {
            return new ResponseHandler().generateResponse("Barang tidak tersedia di cabang Anda",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04034", request);
        }
        BarangCabang barangCabang = optionalBarangCabang.get();


        if (barangGudangRepo.existsByBarangCabangAndIsActive(barangCabang, true)) {
            return new ResponseHandler().generateResponse("Barang sudah tercatat di cabang ini!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04035", request);
        }

        try {
            BarangGudang barangGudang = new BarangGudang();
            barangGudang.setBarangCabang(barangCabang);
            barangGudang.setJumlah(barangGudangDTO.getJumlah());
            barangGudang.setActive(true);
            barangGudang.setCreateApproved(false);
            barangGudang.setDeleteToBeApprove(false);
            barangGudang.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));

            barangGudangRepo.save(barangGudang);

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

    public Page<BarangGudangDTO> findAll(Boolean ca, Boolean da, String kategori, Pageable pageable, String searchTerm, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        String cabangUser = mapToken.get("cg").toString();

        Page<BarangGudang> barangGudangPage;
        KategoriBarang k = null;

        if (kategori != null && !kategori.trim().isEmpty()) {
            Optional<KategoriBarang> optionalKategori = kategoriBarangRepo.findByNamaKategori(kategori);
            if (optionalKategori.isEmpty()) {
                return Page.empty();
            }

            k = optionalKategori.get();
        }
        barangGudangPage = barangGudangRepo.findAll(true, cabangUser, ca, da, searchTerm, k, pageable);

        return barangGudangPage.map(barangGudang -> {
            BarangGudangDTO dto = new BarangGudangDTO();
            dto.setIdBarangGudang(barangGudang.getIdBarangGudang());
            dto.setNamaBarang(barangGudang.getBarangCabang().getBarang().getNamaBarang());
            dto.setKodeBarang(barangGudang.getBarangCabang().getBarang().getKodeBarang());
            dto.setJumlah(barangGudang.getJumlah());
            dto.setSatuan(barangGudang.getBarangCabang().getBarang().getSatuan());
            dto.setKategori(barangGudang.getBarangCabang().getBarang().getKategoriBarang().getNamaKategori());
            return dto;
        });
    }


    public ResponseEntity<Object> approveCreate(Long id, Boolean app, HttpServletRequest request) {

        Optional<BarangGudang> optionalBarangGudang = barangGudangRepo.findById(id);
        if (optionalBarangGudang.isEmpty()) {
            return new ResponseHandler().generateResponse("Item tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }
        BarangGudang barangGudang = optionalBarangGudang.get();

        try {
            if (app) {
                barangGudang.setCreateApproved(true);
            } else {
                barangGudang.setActive(false);
            }
            barangGudang.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            barangGudang.setUpdatedAt(new Date());

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

    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<BarangGudang> optionalBarangGudang = barangGudangRepo.findById(id);
        if (optionalBarangGudang.isEmpty()) {
            return new ResponseHandler().generateResponse("Item tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }
        BarangGudang barangGudang = optionalBarangGudang.get();

        try {
            barangGudang.setDeleteToBeApprove(true);
            barangGudang.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            barangGudang.setUpdatedAt(new Date());

            barangGudangRepo.save(barangGudang);
        } catch (Exception e) {

            new LogTable().inputLogRequest(strExceptionArr, e, OtherConfig.getFlagLogTable(), Long.parseLong(mapToken.get("de").toString()), OtherConfig.getUrlAPILogRequestError(), request);
            return new ResponseHandler().generateResponse("Data Gagal Dihapus",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04031", request);//FAILED VALIDATION
        }
        return new ResponseHandler().generateResponse("Data Berhasil Dihapus!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> approveDelete(Long id, Boolean app, HttpServletRequest request) {

        Optional<BarangGudang> optionalBarangGudang = barangGudangRepo.findById(id);
        if (optionalBarangGudang.isEmpty()) {
            return new ResponseHandler().generateResponse("Item tidak ditemukan",
                    HttpStatus.NOT_FOUND,
                    null,
                    null,
                    request);
        }
        BarangGudang barangGudang = optionalBarangGudang.get();

        try {
            if (app) {
                barangGudang.setCreateApproved(false);
                barangGudang.setDeleteToBeApprove(false);
                barangGudang.setActive(false);
            } else {
                barangGudang.setDeleteToBeApprove(false);
            }
            barangGudang.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            barangGudang.setUpdatedAt(new Date());

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

    public ResponseEntity<Object> getListByCabangAndKategoriAndJumlah(String kategori, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<KategoriBarang> optionalKategoriBarang = kategoriBarangRepo.findByNamaKategori(kategori);
        if (optionalKategoriBarang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }
        KategoriBarang k = optionalKategoriBarang.get();

        String cabangUser = mapToken.get("cg").toString();

        try {
            List<String> barangGudang = barangGudangRepo.listByCabangAndKategoriAndJumlah(true,cabangUser,true,false,k).stream()
                    .map(bg -> bg.getBarangCabang().getBarang().getNamaBarang())
                    .collect(Collectors.toList());
            return new ResponseHandler().generateResponse("Data Barang Berhasil Ditemukan!!",
                    HttpStatus.OK,
                    barangGudang,
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

    public ResponseEntity<Object> findByNamaBarang(String nama, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        String cabangUser = mapToken.get("cg").toString();

        Optional<BarangGudang> optionalBarangGudang = barangGudangRepo.findByNamaBarang(true,nama,cabangUser,true,false);
        if (optionalBarangGudang.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }
        BarangGudang barangGudang = optionalBarangGudang.get();

        BarangGudangDTO barangGudangDTO = modelMapper.map(barangGudang, new TypeToken<BarangGudangDTO>() {
        }.getType());

        return new ResponseHandler().generateResponse("Data Ditemukan!!",
                HttpStatus.OK,
                barangGudangDTO,
                null, request);
    }



}

