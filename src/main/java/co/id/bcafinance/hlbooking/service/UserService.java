package co.id.bcafinance.hlbooking.service;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:49
@Last Modified 05/05/2024 21:49
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.dto.UserDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangDTO;
import co.id.bcafinance.hlbooking.handler.RequestCapture;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.Role;
import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.model.projectakhir.Unit;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import co.id.bcafinance.hlbooking.repo.RoleRepo;
import co.id.bcafinance.hlbooking.repo.UserRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.UnitRepo;
import co.id.bcafinance.hlbooking.util.GlobalFunction;
import co.id.bcafinance.hlbooking.util.LogTable;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import org.apache.kafka.common.protocol.types.Field;
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

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService{
    private String[] strExceptionArr = new String[2];
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UnitRepo unitRepo;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String, Object> mapToken = new HashMap<>();
    @Autowired
    private ModulAuthority modulAuthority;
    @Autowired
    private GlobalFunction globalFunction;

    /**
     * Method untuk update data user berdasarkan id user
     *
     * @param id             Id dari user.
     * @param userDTO        Data user
     * @param request        Permintaan HTTP.
     * @return               ResponseEntity yang berisi hasil dari operasi.
     */
    public ResponseEntity<Object> edit(Long id, UserDTO userDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<User> optionalUser = userRepo.findByIdUserAndIsActive(id, true);
        if (optionalUser.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        User u = optionalUser.get();

        Optional<Unit> optionalUnit = unitRepo.findByNamaUnitAndIsActive(userDTO.getUnit(), true);
        if (optionalUnit.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }
        Unit ut = optionalUnit.get();

        Optional<Role> optionalRole = roleRepo.findByNamaRoleAndIsActive(userDTO.getRole(), true);
        if (optionalRole.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }
        Role r = optionalRole.get();

        try {
            u.setNama(userDTO.getNama());
            u.setCabang(userDTO.getCabang());
            u.setJabatan(userDTO.getJabatan());
            u.setRole(r);
            u.setUnit(ut);
            u.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            u.setUpdatedAt(new Date());
        } catch (Exception e) {
            strExceptionArr[1] = "edit(Long id, User user, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            new LogTable().inputLogRequest(strExceptionArr, e, OtherConfig.getFlagLogTable(), Long.parseLong(mapToken.get("de").toString()), OtherConfig.getUrlAPILogRequestError(), request);
            return new ResponseHandler().generateResponse("Data Gagal Diubah !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04021", request);
        }
        return new ResponseHandler().generateResponse("Data Berhasil Diubah!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    /**
     * Method untuk delete data user berdasarkan id user
     *
     * @param id             Id dari user.
     * @param request        Permintaan HTTP.
     * @return               ResponseEntity yang berisi hasil dari operasi.
     */
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<User> optionalUser = userRepo.findByIdUserAndIsActive(id, true);
        if (optionalUser.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        User u = optionalUser.get();

        try {
            u.setActive(false);
            u.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            u.setUpdatedAt(new Date());

            userRepo.save(u);
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

    /**
     * Method untuk mencari data user berdasarkan id user
     *
     * @param id             Id dari user.
     * @param request        Permintaan HTTP.
     * @return               ResponseEntity yang berisi hasil dari operasi.
     */
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<User> optionalUser = userRepo.findByIdUserAndIsActive(id, true);
        if (optionalUser.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }
        User user = optionalUser.get();

//        JIKA MAU PAKE AUTHENTICATION AS A PARAMETER:
//        boolean hasAdminRole = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
//
//        if (!hasAdminRole && !user.getUsername().equals(authentication.getName())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Anda tidak diizinkan untuk melihat data pengguna lain.");
//        }

        UserDTO userDTO = modelMapper.map(user, new TypeToken<UserDTO>() {
        }.getType());

        return new ResponseHandler().generateResponse("Data Ditemukan!!",
                HttpStatus.OK,
                userDTO,
                null, request);
    }

    /**
     * Method untuk mencari semua data user
     *
     * @param pageable       Paging.
     * @param searchTerm     Filter untuk melakukan pencarian user tertentu.
     * @return               Data semua user.
     */
//    public Page<UserDTO> findUserByRole(String role, Pageable pageable, String searchTerm, HttpServletRequest request) {
//        Optional<Role> optionalRole = roleRepo.findByNamaRoleAndIsActive(role, true);
//        if (optionalRole.isEmpty()) {
//            return Page.empty();
//        }
//
//        Role r = optionalRole.get();
//
//        Page<User> userPage;
//        userPage = userRepo.findByIDRoleAndSearchTermAndIsActive(r, true, searchTerm, pageable);
//
//
//        return userPage.map(user -> {
//            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
//            userDTO.setUnit(user.getUnit().getNamaUnit());
//            return userDTO;
//        });
//    }

    public Page<UserDTO> findAll(Boolean app, String role, Pageable pageable, String searchTerm, HttpServletRequest request) {
        Page<User> userPage;

        Role r = null;

        if (role != null && !role.trim().isEmpty()) {
            Optional<Role> optionalRole = roleRepo.findByNamaRoleAndIsActive(role, true);
            if (optionalRole.isEmpty()) {
                return Page.empty();
            }

            r = optionalRole.get();
        }
        userPage = userRepo.findAll(r,true, app, searchTerm, pageable);

        return userPage.map(user -> modelMapper.map(user, UserDTO.class));
    }

    public ResponseEntity<Object> getCurrentUserInfo(HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<User> optionalUser = userRepo.findById(Long.parseLong(mapToken.get("de").toString()));
        if (optionalUser.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }
        User user = optionalUser.get();

        UserDTO userDTO = modelMapper.map(user, new TypeToken<UserDTO>() {
        }.getType());

        return new ResponseHandler().generateResponse("Data Ditemukan!!",
                HttpStatus.OK,
                userDTO,
                null, request);
    }


}

