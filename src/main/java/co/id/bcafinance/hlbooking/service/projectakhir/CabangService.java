package co.id.bcafinance.hlbooking.service.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/05/2024 13:17
@Last Modified 28/05/2024 13:17
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.projectakhir.Cabang;
import co.id.bcafinance.hlbooking.repo.projectakhir.CabangRepo;
import co.id.bcafinance.hlbooking.util.GlobalFunction;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CabangService {

    @Autowired
    CabangRepo cabangRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GlobalFunction globalFunction;

    @Autowired
    private JdbcTemplate sqlServerJdbcFinancore;

    private String[] strExceptionArr = new String[2];

    public ResponseEntity<Object> getCabangFina() {
        try {
            String sql = "SELECT DISTINCT g.AreaName AS Area " +
                    "FROM genArea g WITH (NOLOCK)";

            List<Map<String, Object>> result = sqlServerJdbcFinancore.queryForList(sql);

            if (!result.isEmpty()) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "getCabangFina()";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<Object> getAllCabang(HttpServletRequest request) {
        try {
//            ResponseEntity<Object> response = getCabangFina();
//            if (response.getStatusCode() == HttpStatus.OK) {
//                List<Map<String, Object>> resultList = (List<Map<String, Object>>) response.getBody();
//                List<String> cabang = resultList.stream()
//                        .map(map -> (String) map.get("Area"))
//                        .collect(Collectors.toList());

            // Ganti kode ini
            List<String> cabang = cabangRepo.findAll().stream()
                    .map(Cabang::getNamaCabang)
                    .collect(Collectors.toList());
            // Sampai sini


                return new ResponseHandler().generateResponse("Data Cabang Berhasil Ditemukan!!",
                        HttpStatus.OK,
                        cabang,
                        null,
                        request);
//            } else {
//                return new ResponseHandler().generateResponse("Data Cabang Tidak Ditemukan!!",
//                        HttpStatus.NOT_FOUND,
//                        null,
//                        "FE04027",
//                        request);
//            }
        } catch (Exception e) {
            return new ResponseHandler().generateResponse("Data Cabang Gagal Ditemukan!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04026",
                    request);
        }
    }
}

