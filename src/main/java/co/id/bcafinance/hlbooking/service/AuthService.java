package co.id.bcafinance.hlbooking.service;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:46
@Last Modified 05/05/2024 21:46
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.LDAPConfig;
import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.configuration.SMTPConfig;
import co.id.bcafinance.hlbooking.core.BcryptImpl;
import co.id.bcafinance.hlbooking.core.Crypto;
import co.id.bcafinance.hlbooking.core.security.JwtUtility;
import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.dto.UserDTO;
import co.id.bcafinance.hlbooking.dto.auth.LoginDTO;
import co.id.bcafinance.hlbooking.dto.auth.RegisDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.UnitDTO;
import co.id.bcafinance.hlbooking.handler.RequestCapture;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.Role;
import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.model.projectakhir.Unit;
import co.id.bcafinance.hlbooking.repo.AuthRepo;
import co.id.bcafinance.hlbooking.repo.RoleRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.CabangRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.UnitRepo;
import co.id.bcafinance.hlbooking.util.ConstantMessages;
import co.id.bcafinance.hlbooking.util.ExecuteSMTP;
import co.id.bcafinance.hlbooking.util.LogTable;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class AuthService implements UserDetailsService {
    private String[] strExceptionArr = new String[2];
    private String[] strEmailParam = new String[3];
    @Autowired
    private ModulAuthority modulAuthority;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private AuthRepo authRepo;
    @Autowired
    private UnitRepo unitRepo;
    @Autowired
    CabangRepo cabangRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private LDAPConfig ldapConfig;
    @Autowired
    private JdbcTemplate sqlServerJdbcHRIS;
    @Autowired
    private JdbcTemplate sqlServerJdbcFinancore;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String, Object> mapToken = new HashMap<>();

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> opUser = authRepo.findTop1ByUsernameAndIsApproved(s, true);
        if(opUser.isEmpty())
        {
            throw new UsernameNotFoundException("User not found with username: " + s);
        }
        User userNext = opUser.get();

        return new org.springframework.security.core.userdetails.
                User(userNext.getUsername(),"",new ArrayList<>());
    }

    public ResponseEntity<Object> regis(RegisDTO regisDTO, HttpServletRequest request) {
        if(!regisDTO.getUsername().isEmpty() || !regisDTO.getCabang().isEmpty())
        {
            return new ResponseHandler().generateResponse(
                    "Data tidak Valid",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FVRGS001",
                    request
            );
        }

        Optional<User> opUserResult = authRepo.findTop1ByUsernameAndIsActive(regisDTO.getUsername(), true);

        try{
            if(!opUserResult.isEmpty())
            {
                User nextUser = opUserResult.get();
                if(nextUser.getIsApproved())
                {
                    if(nextUser.getUsername().equals(regisDTO.getUsername()))
                    {
                        return new ResponseHandler().generateResponse("ANDA SUDAH TERDAFTAR !!",
                                HttpStatus.NOT_ACCEPTABLE,null,"FVRGS002",request);
                    }
                } else {
                    return new ResponseHandler().generateResponse("AKUN ANDA BELUM DIAPPROVE !!",
                            HttpStatus.NOT_ACCEPTABLE,null,"FVRGS002",request);
                }
            } else {
                ResponseEntity<Object> hrisResponse = getInfoHRIS(regisDTO.getUsername());

                if (hrisResponse.getStatusCode() == HttpStatus.OK) {
                    List<Map<String, Object>> hrisData = (List<Map<String, Object>>) hrisResponse.getBody();

                    if (!hrisData.isEmpty()) {
                        Map<String, Object> userData = hrisData.get(0);

                        Optional<Unit> optionalUnit = unitRepo.findByNamaUnitAndIsActive(((String) userData.get("Departemen")), true);

                        Unit unit;
                        if (optionalUnit.isEmpty()) {
                            unit = new Unit();
                            unit.setNamaUnit((String) userData.get("Departemen"));
                            unit.setActive(true);
                            unit = unitRepo.save(unit);
                        } else {
                            unit = optionalUnit.get();
                        }

                        User user = new User();

                        user.setUsername(regisDTO.getUsername());
                        user.setNama((String) userData.get("Nama"));
                        user.setCabang(regisDTO.getCabang());
                        user.setUnit(unit);
                        user.setJabatan((String) userData.get("Jabatan"));
                        user.setIsApproved(false);
                        user.setActive(true);

                        authRepo.save(user);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DATA TIDAK DITEMUKAN DI HRIS");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("GAGAL MENGAMBIL DATA DARI HRIS");
                }
            }
        } catch (Exception e)
        {
            strExceptionArr[1] = "doRegis(User user, HttpServletRequest request)  --- LINE 130 \n ALL - REQUEST"+ RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            new LogTable().inputLogRequest(strExceptionArr,e, OtherConfig.getFlagLogTable(),1L,OtherConfig.getUrlAPILogRequestError(),request);
            return new ResponseHandler().generateResponse("PROSES GAGAL",HttpStatus.INTERNAL_SERVER_ERROR,null,"FERGS001",request);
        }

        return new ResponseHandler().generateResponse("SILAHKAN TUNGGU APPROVAL",
                HttpStatus.CREATED,null,null,request);
    }

    public ResponseEntity<Object> getInfoHRIS(String username) {
        try {
            String sql = "SELECT DISTINCT h.id, h.c_name AS Nama, " +
                    "COALESCE ( t.c_name, '-' ) AS Jabatan, " +
                    "COALESCE ( m.c_name, '-' ) AS Departemen " +
                    "FROM app_fd_bcafs_hris h WITH (NOLOCK) LEFT JOIN dir_user du WITH (NOLOCK)  ON ( h.id = du.username ) " +
                    "LEFT JOIN app_fd_bcafs_title t WITH (NOLOCK) ON ( h.c_titleId = t.id ) " +
                    "LEFT JOIN app_fd_bcafs_division m WITH (NOLOCK) ON ( t.c_divisionId = m.id) " +
                    "WHERE h.c_active = 'Yes' AND h.id = ?";

            List<Map<String, Object>> result = sqlServerJdbcHRIS.queryForList(sql, username);

            if (!result.isEmpty()) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "getInfoHRIS(String username)";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<Object> approve(Long id, UserDTO userDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        if(userDTO.getRole() == null)
        {
            return new ResponseHandler().generateResponse(
                    "ROLE TIDAK BOLEH KOSONG",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FVRGS001",
                    request
            );
        }

        Optional<Role> optionalRole = roleRepo.findByNamaRoleAndIsActive(userDTO.getRole(), true);
        Role r = optionalRole.get();

        Optional<Unit> optionalUnit = unitRepo.findByNamaUnitAndIsActive(userDTO.getUnit(), true);
        Unit ut = optionalUnit.get();
        if (ut.getUnitGroup() == null) {
            return new ResponseHandler().generateResponse(
                    "DEPARTEMEN DARI USER BERIKUT BELUM MEMILIKI GRUP DEPARTEMEN. " +
                            "SILAHKAN TETAPKAN GRUP DEPARTEMEN PADA MENU DEPARTEMEN!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FVRGS001",
                    request
            );
        }

        Optional<User> optionalUser = authRepo.findById(id);
        try
        {
            if(!optionalUser.isEmpty())
            {
                User u = optionalUser.get();

                u.setRole(r);
                u.setIsApproved(true);
                u.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
                u.setUpdatedAt(new Date());
            }
            else
            {
                return new ResponseHandler().generateResponse("USER TIDAK TERDAFTAR",
                        HttpStatus.NOT_FOUND,null,"FVRGS012",request);
            }
        }
        catch (Exception e)
        {
            strExceptionArr[1]="doVerification(User user)  --- LINE 164";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLoging());

            new LogTable().inputLogRequest(strExceptionArr,e, OtherConfig.getFlagLogTable(),1L,OtherConfig.getUrlAPILogRequestError(),request);
            return new ResponseHandler().generateResponse("PROSES TIDAK WAJAR",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FERGS013",request);
        }

        return new ResponseHandler().generateResponse("APPROVE BERHASIL",
                HttpStatus.OK,null,null,request);
    }


    /**
     * Method untuk melakukan login akun user
     *
     * @param loginDTO    Data user yang akan disimpan.
     * @param request Permintaan HTTP.
     * @return        ResponseEntity yang token jwt user.
     */
    public ResponseEntity<Object> login(LoginDTO loginDTO, HttpServletRequest request) {
        User u = null;

        try {
            if (loginDTO.getUsername().isEmpty() || loginDTO.getPassword().isEmpty()) {
                return new ResponseHandler().generateResponse("LOGIN INVALID",
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV01001",
                        request);
            }

//            Boolean isLogin = checkLdapLogin(loginDTO, request);
//
//            if (!isLogin) {
//                return new ResponseHandler().generateResponse("LOGIN INVALID",
//                        HttpStatus.UNAUTHORIZED,
//                        null,
//                        "FV01002",
//                        request);
//            }

            Optional<User> optionalUser = authRepo.findTop1ByUsernameAndIsActive(loginDTO.getUsername(), true);

            if(!optionalUser.isEmpty()) {
                u = optionalUser.get();
                if(!u.getIsApproved())
                {
                    return new ResponseHandler().generateResponse("SILAHKAN TUNGGU APPROVAL",
                            HttpStatus.NOT_ACCEPTABLE,null,"FV01006",request);
                }

                Boolean cabangExist = checkCabangFina(u.getCabang());
                if (!cabangExist) {
                    u.setActive(false);
                    return new ResponseHandler().generateResponse("CABANG ANDA TIDAK TERDAFTAR. SILAHKAN LAKUKAN REGISTRASI ULANG",
                            HttpStatus.NOT_ACCEPTABLE,null,"FV01008",request);
                }
            } else {
                return new ResponseHandler().generateResponse("User Tidak Terdaftar",
                        HttpStatus.NOT_ACCEPTABLE,null,"FV01008",request);
            }
        } catch (IllegalStateException e) {
            strExceptionArr[1]="doLogin(User user,WebRequest request)  --- LINE 952";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLoging());
            new LogTable().inputLogRequest(strExceptionArr,e, OtherConfig.getFlagLogTable(),1L,OtherConfig.getUrlAPILogRequestError(),request);
            return new ResponseHandler().generateResponse("Segera Hubungi Administrator / Customer Service terdekat !!",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FE04004",request);
        } catch (Exception e) {
            strExceptionArr[1]="doLogin(User user,WebRequest request)  --- LINE 962";
            LoggingFile.exceptionStringz(strExceptionArr,e, OtherConfig.getFlagLoging());
            new LogTable().inputLogRequest(strExceptionArr,e, OtherConfig.getFlagLogTable(),1L,OtherConfig.getUrlAPILogRequestError(),request);
            return new ResponseHandler().generateResponse("Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FE04005",request);
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",authManager(u,request));
        map.put("id",u.getIdUser());

        return new ResponseHandler().generateResponse("Login Berhasil",
                HttpStatus.CREATED,map,null,request);
    }

    private boolean checkLdapLogin(LoginDTO user, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String ldapUrl = LDAPConfig.getLdapUrl();

        // Prepare the request payload
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("UserId", user.getUsername());
        credentials.put("UserName", null);
        credentials.put("Password", user.getPassword());

        Map<String, Object> payload = new HashMap<>();
        payload.put("TrxId", null);
        payload.put("Credentials", credentials);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBasicAuth(LDAPConfig.getBasicAuthUname(), LDAPConfig.getBasicAuthPw());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(ldapUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    Map<String, Object> responseHeader = (Map<String, Object>) responseBody.get("ResponseHeader");
                    if (responseHeader != null && "Success".equals(responseHeader.get("ErrorDescription").toString())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            strExceptionArr[1] = "checkLdapLogin(LoginDTO)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
        }

        return false;
    }

    public boolean checkCabangFina(String cabang) {
        try {
//            String sql = "SELECT COUNT(*) " +
//                    "FROM genArea g WITH (NOLOCK) " +
//                    "WHERE g.AreaName = ?";
//
//            int count = sqlServerJdbcFinancore.queryForObject(sql, Integer.class, cabang);
//            return count > 0;

            // Change here
            return cabangRepo.existsByNamaCabangAndIsActive(cabang, true);

        } catch (Exception e) {
            strExceptionArr[1] = "checkCabangFina(String cabang)";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return false;
        }
    }

    public String authManager(User user, HttpServletRequest request) {
        UserDetails userDetails = loadUserByUsername(user.getUsername());
        if(userDetails == null) {
            return "FAILED";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("de", user.getIdUser());
        map.put("cg", user.getCabang());
        map.put("ut", user.getUnit());
        map.put("nu", user.getNama());
        map.put("ur", user.getUsername());

        Role userRole = user.getRole();
        if (userRole != null) {
            map.put("re", userRole.getNamaRole());
        } else {
            map.put("re", "UNDEFINED");
        }

        String token = jwtUtility.generateToken(userDetails, map);
        token = Crypto.performEncrypt(token);

        return token;
    }

    public Page<UserDTO> findAll(Pageable pageable, String searchTerm, HttpServletRequest request) {
        Page<User> userPage;
        userPage = authRepo.findAllNotApprovedUserBySearchTerm(true, false, searchTerm, pageable);

        return userPage.map(user -> modelMapper.map(user, UserDTO.class));
    }

    /**
     * Method untuk melakukan pengecekan role dari akun yang sedang login
     *
     * @return        Role dari akun tersebut.
     */
    public String checkUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().toString();
        return role.contains("ROLE_ADMIN") ? "ADMIN" : "MEMBER";
    }


}

