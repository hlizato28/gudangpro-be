package id.bcafinance.retrieveacmbackend.service;

import id.bcafinance.retrieveacmbackend.configuration.LDAPConfig;
import id.bcafinance.retrieveacmbackend.configuration.OtherConfig;
import id.bcafinance.retrieveacmbackend.core.BcryptImpl;
import id.bcafinance.retrieveacmbackend.core.Crypto;
import id.bcafinance.retrieveacmbackend.core.security.JwtUtility;
import id.bcafinance.retrieveacmbackend.dto.SearchParamDTO;
import id.bcafinance.retrieveacmbackend.dto.user.FindAllUserDTO;
import id.bcafinance.retrieveacmbackend.dto.user.LoginDTO;
import id.bcafinance.retrieveacmbackend.dto.user.RegisterInternalDTO;
import id.bcafinance.retrieveacmbackend.handler.RequestCapture;
import id.bcafinance.retrieveacmbackend.handler.ResponseHandler;
import id.bcafinance.retrieveacmbackend.model.MenuModel;
import id.bcafinance.retrieveacmbackend.model.RoleModel;
import id.bcafinance.retrieveacmbackend.model.UserModel;
import id.bcafinance.retrieveacmbackend.repo.RoleRepo;
import id.bcafinance.retrieveacmbackend.repo.UserExternalRepo;
import id.bcafinance.retrieveacmbackend.repo.UserRepo;
import id.bcafinance.retrieveacmbackend.util.ConstantMessage;
import id.bcafinance.retrieveacmbackend.util.LocalDateTimeAttributeConverter;
import id.bcafinance.retrieveacmbackend.util.LoggingFile;
import id.bcafinance.retrieveacmbackend.util.TransformToDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * UserService
 * Module Code 01
 */

@Service
@Transactional
public class UserService implements UserDetailsService {
    private Map<String, Object> mapz = new HashMap<>();
    private StringBuilder sBuild = new StringBuilder();
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserExternalRepo userExternalRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private LocalDateTimeAttributeConverter localDateTimeAttributeConverter;
    @Autowired
    private ConstantMessage constantMessage;
    @Autowired
    private LDAPConfig ldapConfig;
    @Autowired
    private JdbcTemplate sqlServerJdbcTemplate;

    Map<String, Object> mapColumn = new HashMap<>();

    TransformToDTO transformToDTO = new TransformToDTO();
    private List<SearchParamDTO> listSearchParamDTO = new ArrayList<>();

    Map<String, Object> mapResult = new HashMap<>();

    private String[] strExceptionArr = new String[2];

    public UserService() {
        strExceptionArr[0] = "UserService";
        mapColumn.put("userId", "userId");
        mapColumn.put("username", "username");
        mapColumn.put("email", "email");
        mapColumn.put("roleModel", "roleModel");
        mapColumn.put("isLogin", "isLogin");
        mapColumn.put("lastAccess", "lastAccess");
        mapColumn.put("createdAt", "createdAt");
        mapColumn.put("createdBy", "createdBy");
        forComponentFiltering();
    }

    private void forComponentFiltering() {
        listSearchParamDTO.add(new SearchParamDTO("userId", "userId"));
        listSearchParamDTO.add(new SearchParamDTO("username", "username"));
        listSearchParamDTO.add(new SearchParamDTO("email", "email"));
        listSearchParamDTO.add(new SearchParamDTO("roleModel", "roleModel"));
        listSearchParamDTO.add(new SearchParamDTO("isLogin", "isLogin"));
        listSearchParamDTO.add(new SearchParamDTO("ipLogin", "ipLogin"));
        listSearchParamDTO.add(new SearchParamDTO("createdAt", "createdAt"));
        listSearchParamDTO.add(new SearchParamDTO("createdBy", "createdBy"));
    }


    /**
     * Find All User
     *
     * @param pageable
     * @param filterBy
     * @param filterValue
     * @param request
     * @return ResponseEntity
     */
    public ResponseEntity<Object> findAll(
            Pageable pageable,
            String filterBy,
            String filterValue,
            HttpServletRequest request
    ) {
        Page<UserModel> pageUser = null;
        List<UserModel> listUser = null;

        try {
            if (filterBy == null || filterBy.equals("") || filterValue == null || filterValue.equals("")) {
                pageUser = getDataByValue(pageable);
            } else {
                pageUser = getDataByValue(pageable, filterBy, filterValue);
            }

            listUser = pageUser.getContent();
            if (listUser.isEmpty()) {
                ArrayList<Object> emptyList = new ArrayList<>();
                return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                        HttpStatus.OK,
                        emptyList,
                        "FV01001", request);
            }

            List<FindAllUserDTO> listUserDTO = modelMapper.map(listUser, new TypeToken<List<FindAllUserDTO>>() {
            }.getType());

            List<Map<String, Object>> data = new ArrayList<>();

            listUserDTO.forEach(user -> {
                Map<String, Object> map = new HashMap<>();
                map.put("userId", user.getUserId());
                map.put("username", user.getUsername());
                map.put("email", user.getEmail());
                map.put("roleModel", user.getRoleModel().getRoleName());
                map.put("lastAccess", user.getLastAccess());
                map.put("createdAt", user.getCreatedAt());
                map.put("divisi", user.getDivisi());
                map.put("cabang", user.getCabang());
                map.put("jabatan", user.getJabatan());
                map.put("fullName", user.getFullName());
                map.put("departemen", user.getDepartemen());
                Optional<UserModel> opResult = userRepo.findOneByUserId(user.getCreatedBy());
                UserModel userResult = opResult.get();
                map.put("createdBy", userResult.getFullName());
                data.add(map);
            });

            mapResult = transformToDTO.transformObject(mapResult,
                    data,
                    pageUser,
                    filterBy,
                    filterValue,
                    listSearchParamDTO);

            return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                    HttpStatus.OK,
                    mapResult,
                    null,
                    request);
        } catch (Exception e) {
            strExceptionArr[1] = "findAll(Pageable, String, String, HttpServletRequest)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return new ResponseHandler().generateResponse(constantMessage.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE01001",
                    request);
        }
    }

    /**
     * Register User Internal
     *
     * @param registerInternalDTO
     * @param request
     * @return ResponseEntity
     */
    public ResponseEntity<Object> registerUserInternal(RegisterInternalDTO registerInternalDTO, HttpServletRequest request) {
        try {
            Optional<UserModel> opUser = userRepo.findOneByUsername(registerInternalDTO.getUsername());

            if (opUser.isPresent()) {
                if (opUser.get().getDeleted()) {
                    UserModel userExist = opUser.get();
                    userExist.setDeleted(false);
                    userExist.setDeletedBy(null);

                    return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                            HttpStatus.CREATED,
                            null,
                            null,
                            request);
                }
                return new ResponseHandler().generateResponse(constantMessage.USER_EXIST,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV01002",
                        request);
            }

            Optional<RoleModel> roleResult = roleRepo.findOneByRoleId(registerInternalDTO.getRoleId());
            if (roleResult.isEmpty()) {
                return new ResponseHandler().generateResponse(constantMessage.ROLE_NOT_FOUND,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV01003",
                        request);
            }

            String userAuthId = jwtUtility.getIdFromAuthorization(request.getHeader("Authorization"));

            UserModel userModel = new UserModel();
            userModel.setEmail(registerInternalDTO.getEmail());
            userModel.setToken(null);
            userModel.setTrxId(null);

            userModel.setCabang(registerInternalDTO.getCabang());
            userModel.setDivisi(registerInternalDTO.getDivisi());
            userModel.setJabatan(registerInternalDTO.getJabatan());
            userModel.setUsername(registerInternalDTO.getUsername());
            userModel.setDepartemen(registerInternalDTO.getDepartemen());
            userModel.setPassword(null);
            userModel.setDivisiId(registerInternalDTO.getDivisiId());
            userModel.setFullName(registerInternalDTO.getFullName());
            userModel.setCreatedAt(new Date());
            userModel.setCreatedBy(Long.parseLong(userAuthId));
            userModel.setUpdatedAt(null);
            userModel.setUpdatedBy(null);
            userModel.setDeleted(false);
            userModel.setDeletedAt(null);

            userModel.setRoleModel(roleResult.get());
            userRepo.save(userModel);

            return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                    HttpStatus.CREATED,
                    null,
                    null,
                    request);
        } catch (Exception e) {
            strExceptionArr[1] = "registerUserInternal(RegisterInternalDTO, HttpServletRequest)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse(constantMessage.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE01002",
                    request);
        }
    }

    /**
     * Login User Internal using LDAP
     *
     * @param loginDTO
     * @param request
     * @return ResponseEntity
     * @throws UsernameNotFoundException
     */
    public ResponseEntity<Object> loginUserInternal(LoginDTO loginDTO, HttpServletRequest request) {
        try {
            if (loginDTO.getUsername().isEmpty() || loginDTO.getPassword().isEmpty()) {
                return new ResponseHandler().generateResponse(constantMessage.LOGIN_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV01001",
                        request);
            }

            Boolean isLogin = checkLdapLogin(loginDTO, request);

            if (!isLogin) {
                return new ResponseHandler().generateResponse(constantMessage.LOGIN_INVALID,
                        HttpStatus.UNAUTHORIZED,
                        null,
                        "FV01002",
                        request);
            }

            Optional<UserModel> userExist = userRepo.findOneByUsername(loginDTO.getUsername());

            if (userExist.isEmpty()) {
                return new ResponseHandler().generateResponse(constantMessage.NOT_REGISTERED,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV01003",
                        request);
            }

            UserModel userModel = userExist.get();

            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Object> role = new HashMap<>();
            List<Map<String, Object>> menu = new ArrayList<>();

            role.put("roleName", userModel.getRoleModel().getRoleName());

            for (MenuModel menuModel : userModel.getRoleModel().getMenuModel()) {
                Map<String, Object> menuMap = new HashMap<>();
                menuMap.put("menuName", menuModel.getMenuName());
                menuMap.put("menuUrl", menuModel.getMenuUrl());
                menu.add(menuMap);
            }

            userModel.setPassword(BcryptImpl.hash(loginDTO.getPassword() + loginDTO.getUsername()));

            String token = authManager(userModel, request);

            map.put("token", token);
            map.put("role", role);
            map.put("menu", menu);

            userModel.setToken(token);


            return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                    HttpStatus.OK,
                    map,
                    null,
                    request);
        } catch (Exception e) {
            strExceptionArr[1] = "loginUserInternal(LoginDTO, HttpServletRequest)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse(constantMessage.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE01001",
                    request);
        }
    }


    /**
     * Delete User By Id
     *
     * @param userId
     * @param request
     * @return ResponseEntity<Object>
     */
    public ResponseEntity<Object> deleteById(Long userId, HttpServletRequest request) {
        try {
            Optional<UserModel> opUser = userRepo.findOneByUserId(userId);
            if (opUser.isEmpty()) {
                return new ResponseHandler().generateResponse(constantMessage.DATA_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        null,
                        "FV01004", request);
            }

            String userAuthId = jwtUtility.getIdFromAuthorization(request.getHeader("Authorization"));

            UserModel user = opUser.get();
            user.setDeletedBy(Long.parseLong(userAuthId));
            user.setDeleted(true);
            user.setDeletedAt(new Date());

            return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                    HttpStatus.OK,
                    null,
                    null,
                    request);
        } catch (Exception e) {
            strExceptionArr[1] = "deleteById(Long userId, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return new ResponseHandler().generateResponse(constantMessage.FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE01005", request);
        }
    }

    /**
     * Get Info HRIS
     *
     * @param username
     * @param request
     * @return ResponseEntity<Object>
     */
    public ResponseEntity<Object> getInfoHRIS(String username, HttpServletRequest request) {
        try {
            String sql = "SELECT DISTI NCT h.id, h.c_name AS Nama, " +
                    "h.c_email AS Email, " +
                    "COALESCE ( b.c_location, '-' ) AS Cabang, " +
                    "COALESCE ( d.c_id, '-' ) AS id_divisi, " +
                    "COALESCE ( d.c_name, '-' ) AS Divisi, " +
                    "COALESCE ( t.c_name, '-' ) AS Jabatan, " +
                    "COALESCE ( m.c_name, '-' ) AS Departemen, " +
                    "COALESCE ( h.c_externalUser, 'No' ) AS External_User " +
                    "FROM app_fd_bcafs_hris h WITH (NOLOCK) " +
                    "LEFT JOIN dir_user du WITH (NOLOCK)  ON ( h.id = du.username ) " +
                    "LEFT JOIN app_fd_bcafs_branch b WITH (NOLOCK) ON ( h.c_branchId = b.c_branchCode ) " +
                    "LEFT JOIN app_fd_bcafs_title t WITH (NOLOCK) ON ( h.c_titleId = t.id ) " +
                    "LEFT JOIN app_fd_bcafs_dept m WITH (NOLOCK) ON ( t.c_departmentId = m.id) " +
                    "LEFT JOIN app_fd_bcafs_division d WITH (NOLOCK) ON ( t.c_divisionId = d.id ) " +
                    "WHERE ( h.c_active = 'Yes' OR h.c_externalUser = 'Yes' ) AND h.id = ?";
//            String sql = "Select * from sysdatabases";
//            Map<String, Object> result = sqlServerJdbcTemplate.queryForMap(sql, username);
            List<Map<String, Object>> result = sqlServerJdbcTemplate.queryForList(sql, username);

            return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                    HttpStatus.OK,
                    result.get(0),
                    null,
                    request);
        } catch (Exception e) {
            strExceptionArr[1] = "getInfoHRIS(String username, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return new ResponseHandler().generateResponse(constantMessage.FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE01006", request);
        }
    }

    /**
     * Verify Auth Token
     *
     * @param request
     * @return ResponseEntity<Object>
     */
    public ResponseEntity<Object> verifyAuthToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String userAuthId = jwtUtility.getIdFromAuthorization(token);
        Optional<UserModel> userAuth = userRepo.findOneByUserId(Long.parseLong(userAuthId));
        token = token.substring(7);

        UserModel nextUser = null;
        try {
            if (!userAuth.isEmpty()) {
                nextUser = userAuth.get();

                if (nextUser.getToken() != null) {
                    if (nextUser.getToken().equals(token)) {
                        return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                                HttpStatus.OK,
                                null,
                                null,
                                request);
                    } else {
                        return new ResponseHandler().generateResponse(constantMessage.INVALID_TOKEN,
                                HttpStatus.NOT_ACCEPTABLE,
                                null,
                                "FV01011", request);
                    }
                } else {
                    return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                            HttpStatus.OK,
                            null,
                            null,
                            request);
                }
            }
        } catch (Exception e) {
            strExceptionArr[1] = "verifyAuthToken(HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return new ResponseHandler().generateResponse(constantMessage.FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE01007", request);
        }
        return new ResponseHandler().generateResponse(constantMessage.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                null,
                "FV01012", request);
    }

    /**
     * Multiple Delete
     *
     * @param userIds
     * @param request
     * @return ResponseEntity<Object>
     */
    public ResponseEntity<Object> multipleDelete(Long[] userIds, HttpServletRequest request) {
        try {
            String userAuthId = jwtUtility.getIdFromAuthorization(request.getHeader("Authorization"));

            for (Long userId : userIds) {
                Optional<UserModel> opUser = userRepo.findOneByUserId(userId);

                UserModel user = opUser.get();
                user.setDeletedBy(Long.parseLong(userAuthId));
                user.setDeleted(true);
                user.setDeletedAt(new Date());
            }

            return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                    HttpStatus.OK,
                    null,
                    null,
                    request);
        } catch (Exception e) {
            strExceptionArr[1] = "multipleDelet(Long[] userId, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return new ResponseHandler().generateResponse(constantMessage.FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE01008", request);
        }
    }

    /**
     * Update User
     *
     * @param userId
     * @param roleId
     * @param userModel
     * @param request
     * @return ResponseEntity<Object>
     */
    public ResponseEntity<Object> update(Long userId, Long roleId, UserModel userModel, HttpServletRequest request) {
        try {
            String userAuthId = jwtUtility.getIdFromAuthorization(request.getHeader("Authorization"));
            Optional<UserModel> opUser = userRepo.findOneByUserId(userId);

            if (opUser.isEmpty()) {
                return new ResponseHandler().generateResponse(constantMessage.DATA_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        null,
                        "FV01005", request);
            }

            Optional<RoleModel> roleResult = roleRepo.findOneByRoleId(roleId);

            UserModel user = opUser.get();
            user.setDivisi(userModel.getDivisi());
            user.setDivisiId(userModel.getDivisiId());
            user.setJabatan(userModel.getJabatan());
            user.setCabang(userModel.getCabang());
            user.setRoleModel(roleResult.get());
            user.setFullName(userModel.getFullName());
            user.setEmail(userModel.getEmail());
            user.setDepartemen(userModel.getDepartemen());
            user.setUpdatedAt(new Date());
            user.setUpdatedBy(Long.parseLong(userAuthId));

            return new ResponseHandler().generateResponse(constantMessage.SUCCESS,
                    HttpStatus.OK,
                    null,
                    null,
                    request);
        } catch (Exception e) {
            strExceptionArr[1] = "update(Long userId, Long roleId, UserModel userModel, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return new ResponseHandler().generateResponse(constantMessage.FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE01009", request);
        }
    }

    /**
     * checkLdapLogin
     *
     * @param user
     * @param request
     * @return boolean
     */
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

    /**
     * Generate JWT token
     *
     * @param userModel
     * @param request
     * @return String
     */
    public String authManager(UserModel userModel, HttpServletRequest request)//RANGE 006-010
    {
        /* Untuk memasukkan user ke dalam */
        sBuild.setLength(0);
        UserDetails userDetails = loadUserByUsername(userModel.getUsername());
        if (userDetails == null) {
            return "FAILED";
        }

        /* Isi apapun yang perlu diisi ke dalam object mapz !! */
        mapz.put("uid", userModel.getUserId());
        mapz.put("uname", userModel.getUsername());
        mapz.put("ml", userModel.getEmail());
        mapz.put("rl", userModel.getRoleModel().getRoleName());
        mapz.put("jn", "Internal");

        String token = jwtUtility.generateToken(userDetails, mapz);
        token = Crypto.performEncrypt(token);

        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        /**
         WARNING !!
         username yang ada di parameter otomatis hanya username , bukan string yang di kombinasi dengan password atau informasi lainnya...
         userName yang ada di parameter belum tentu adalah username...
         karena sistem memperbolehkan login dengan email, nohp ataupun username
         pastikan harus mengecek flag user teregistrasi atau belum
         */
        Optional<UserModel> opUser = userRepo.findOneByUsername(s);
        if (opUser.isEmpty()) {
            return null;
        }
        UserModel userNext = opUser.get();
        /**
         PARAMETER KE 3 TIDAK MENGGUNAKAN ROLE DARI SPRINGSECURITY CORE
         ROLE MODEL AKAN DITAMBAHKAN DI METHOD authManager DAN DIJADIKAN INFORMASI DI DALAM JWT AGAR LEBIH DINAMIS
         */
        return new org.springframework.security.core.userdetails.
                User(userNext.getUsername(), userNext.getPassword(), new ArrayList<>());
    }

    /**
     * Get Data Using Filter
     *
     * @param pageable
     * @param columnFirst
     * @param valueFirst
     * @return Page<UserModel>
     */
    private Page<UserModel> getDataByValue(org.springframework.data.domain.Pageable pageable, String columnFirst, String valueFirst) {
        if (columnFirst.equals("id")) {
            return userRepo.findAllByIsDeleted(pageable, false);
        } else if (columnFirst.equals("username")) {
            return userRepo.findByUsernameContainingIgnoreCaseAndIsDeleted(pageable, valueFirst, false);
        }

        return userRepo.findAllByIsDeleted(pageable, false);
    }

    /**
     * Get Data Without Filter
     *
     * @param pageable
     * @return Page<UserModel>
     */
    private Page<UserModel> getDataByValue(org.springframework.data.domain.Pageable pageable) {
        try {
            return userRepo.findAllByIsDeleted(pageable, false);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
        } catch (Exception e) {
            return null;
        }

    }
}
