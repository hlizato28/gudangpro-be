package co.id.bcafinance.hlbooking.service;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 20/06/2024 10:07
@Last Modified 20/06/2024 10:07
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.dto.UserDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.RoleDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.KategoriBarangDTO;
import co.id.bcafinance.hlbooking.handler.RequestCapture;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.Role;
import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import co.id.bcafinance.hlbooking.repo.RoleRepo;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    private GlobalFunction globalFunction;
    @Autowired
    private ModulAuthority modulAuthority;

    @Autowired
    private ModelMapper modelMapper;

    private String[] strExceptionArr = new String[2];

    private Map<String, Object> mapToken = new HashMap<>();

    public ResponseEntity<Object> create(RoleDTO roleDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        String namaRole = roleDTO.getNamaRole();
        if (roleRepo.existsByNamaRoleAndIsActive(namaRole, true)) {
            return new ResponseHandler().generateResponse("Nama Role sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04035", request);
        }

        try {
            Role role = new Role();
            role.setNamaRole(roleDTO.getNamaRole());
            role.setActive(true);
            role.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));

            roleRepo.save(role);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHandler().generateResponse("Role Gagal Dibuat!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04034", request);
        }

        return new ResponseHandler().generateResponse("Role Berhasil Dibuat!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> edit(Long id, RoleDTO roleDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Role> optionalRole = roleRepo.findById(id);
        if (optionalRole.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Role r = optionalRole.get();

        String namaRole = roleDTO.getNamaRole();
        if (roleRepo.existsByNamaRoleAndIsActive(namaRole, true)) {
            return new ResponseHandler().generateResponse("Nama Role sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04035", request);
        }

        try {
            r.setNamaRole(roleDTO.getNamaRole());
            r.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            r.setUpdatedAt(new Date());

            roleRepo.save(r);
        } catch (Exception e) {
            strExceptionArr[1] = "doEdit(Long id, RoleDTO roleDTO, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Role Gagal Diubah !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04023", request);
        }
        return new ResponseHandler().generateResponse("Data Role Berhasil Diubah!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Role> optionalRole = roleRepo.findById(id);
        if (optionalRole.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Role r = optionalRole.get();

        try {
            r.setActive(false);
            r.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            r.setUpdatedAt(new Date());

            roleRepo.save(r);

        } catch (Exception e) {
            strExceptionArr[1] = "doDelete(Long id, HttpServletRequest request) - Role" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Role Gagal Dihapus !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04025", request);
        }
        return new ResponseHandler().generateResponse("Data Role Berhasil Dihapus!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> getListRole(HttpServletRequest request) {
        try {
            List<String> role = roleRepo.findAllByIsActiveOrderByNamaRoleAsc(true).stream()
                    .map(Role::getNamaRole)
                    .collect(Collectors.toList());
            return new ResponseHandler().generateResponse("Data Role Berhasil Ditemukan!!",
                    HttpStatus.OK,
                    role,
                    null,
                    request);
        } catch (Exception e) {
            return new ResponseHandler().generateResponse("Data Role Gagal Ditemukan!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04026",
                    request);
        }
    }

    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<Role> optionalRole = roleRepo.findByIdRoleAndIsActive(id, true);
        if (optionalRole.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }
        Role role = optionalRole.get();

//        JIKA MAU PAKE AUTHENTICATION AS A PARAMETER:
//        boolean hasAdminRole = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
//
//        if (!hasAdminRole && !user.getUsername().equals(authentication.getName())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Anda tidak diizinkan untuk melihat data pengguna lain.");
//        }

        RoleDTO roleDTO = modelMapper.map(role, new TypeToken<RoleDTO>() {
        }.getType());

        return new ResponseHandler().generateResponse("Data Ditemukan!!",
                HttpStatus.OK,
                roleDTO,
                null, request);
    }

    public Page<RoleDTO> findAllRole(Pageable pageable, String searchTerm) {
        Page<Role> rolePage;
        rolePage = roleRepo.findByIsActiveContainingIgnoreCase(true, searchTerm, pageable);

        return rolePage.map(role -> modelMapper.map(role, RoleDTO.class));
    }

}



