//package co.id.bcafinance.hlbooking.service;
///*
//IntelliJ IDEA 2022.3.1 (Community Edition)
//Build #IC-223.8214.52, built on December 20, 2022
//@Author Heinrich a.k.a. Heinrich Lizato
//Java Developer
//Created on 05/05/2024 21:49
//@Last Modified 05/05/2024 21:49
//Version 1.0
//*/
//
//import co.id.bcafinance.hlbooking.broker.HitProducer;
//import co.id.bcafinance.hlbooking.configuration.OtherConfig;
//import co.id.bcafinance.hlbooking.dto.LapanganDTO;
//import co.id.bcafinance.hlbooking.handler.RequestCapture;
//import co.id.bcafinance.hlbooking.handler.ResponseHandler;
//import co.id.bcafinance.hlbooking.model.Lapangan;
//import co.id.bcafinance.hlbooking.repo.LapanganRepo;
//import co.id.bcafinance.hlbooking.util.GlobalFunction;
//import co.id.bcafinance.hlbooking.util.LoggingFile;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class LapanganService {
//    @Autowired
//    LapanganRepo lapanganRepo;
//    @Autowired
//    private ModelMapper modelMapper;
//    @Autowired
//    private GlobalFunction globalFunction;
//    private String[] strExceptionArr = new String[2];
//
//    /**
//     * Method untuk membuat lapangan baru
//     *
//     * @param lapangan       Data lapangan.
//     * @param request        Permintaan HTTP.
//     * @return               ResponseEntity yang berisi hasil dari operasi.
//     */
//    public ResponseEntity<Object> createLapangan(Lapangan lapangan, HttpServletRequest request) {
//        try {
////            lapangan.setCreatedAt(LocalDateTime.now());
////            lapangan.setCreatedBy("admin");
////            lapanganRepo.save(lapangan);
//
//            List<Map<String, Object>> data = new ArrayList<>();
//            Map<String, Object> map = new HashMap<>();
//            map.put("namaLapangan", lapangan.getNamaLapangan());
//            map.put("jamMulai", lapangan.getJamMulai());
//            map.put("jamSelesai", lapangan.getJamSelesai());
//            data.add(map);
//
//            new HitProducer().producerHitTopics(data, "createLapangan");
//        } catch (Exception e) {
//            return new ResponseHandler().generateResponse("Lapangan Gagal Dibuat!!",
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    null,
//                    "FE04031", request);//FAILED VALIDATION
//        }
//        return new ResponseHandler().generateResponse("Lapangan Berhasil Dibuat!!",
//                HttpStatus.OK,
//                null,
//                null, request);
//    }
//
//    /**
//     * Method untuk menghapus data lapangan berdasarkan id lapangan
//     *
//     * @param id             Id dari lapangan.
//     * @param request        Permintaan HTTP.
//     * @return               ResponseEntity yang berisi hasil dari operasi.
//     */
//    public ResponseEntity<Object> deleteLapangan(Long id, HttpServletRequest request) {
//        Optional<Lapangan> optionalLapangan = lapanganRepo.findById(id);
//        if (optionalLapangan.isEmpty()) {
//            return globalFunction.dataNotFoundById(request);
//        }
//
//        try {
//            lapanganRepo.deleteById(id);
//        } catch (Exception e) {
//            return new ResponseHandler().generateResponse("Data Gagal Dihapus",
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    null,
//                    "FE04031", request);//FAILED VALIDATION
//        }
//        return new ResponseHandler().generateResponse("Data Berhasil Dihapus!!",
//                HttpStatus.OK,
//                null,
//                null, request);
//    }
//
//    /**
//     * Method untuk update data lapangan berdasarkan id lapangan
//     *
//     * @param id             Id dari lapangan.
//     * @param lapanganDTO    Data lapangan
//     * @param request        Permintaan HTTP.
//     * @return               ResponseEntity yang berisi hasil dari operasi.
//     */
//    public ResponseEntity<Object> editLapangan(Long id, LapanganDTO lapanganDTO, HttpServletRequest request) {
//        Optional<Lapangan> optionalLapangan = lapanganRepo.findById(id);
//        if (optionalLapangan.isEmpty()) {
//            return globalFunction.dataNotFoundById(request);
//        }
//
//        Lapangan l = optionalLapangan.get();
//
//        try {
//            l.setNamaLapangan(lapanganDTO.getNamaLapangan());
//            l.setJamMulai(lapanganDTO.getJamMulai());
//            l.setJamSelesai(lapanganDTO.getJamSelesai());
//            l.setUpdatedBy("admin");
//            l.setUpdatedAt(new Date());
//        } catch (Exception e) {
//            strExceptionArr[1] = "edit(Long id, Lapangan lapangan, HttpServletRequest request)" + RequestCapture.allRequest(request);
//            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
//
//            return new ResponseHandler().generateResponse("Data Lapangan Gagal Diubah !! ",
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    null,
//                    "FE04021", request);
//        }
//        return new ResponseHandler().generateResponse("Data Lapangan Berhasil Diubah!!",
//                HttpStatus.OK,
//                null,
//                null, request);
//    }
//
//    /**
//     * Method untuk mencari data lapangan berdasarkan id lapangan
//     *
//     * @param id             Id dari lapangan.
//     * @param request        Permintaan HTTP.
//     * @return               ResponseEntity yang berisi hasil dari operasi.
//     */
//    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
//        Optional<Lapangan> optionalLapangan = lapanganRepo.findById(id);
//        if (optionalLapangan.isEmpty()) {
//            return globalFunction.dataNotFoundById(request);
//        }
//
//        Lapangan l = optionalLapangan.get();
//
//        LapanganDTO lapanganDTO = modelMapper.map(l, LapanganDTO.class);
//
//        return new ResponseHandler().generateResponse("Data Ditemukan!!",
//                HttpStatus.OK,
//                lapanganDTO,
//                null, request);
//    }
//
//    /**
//     * Method untuk mencari semua data lapangan
//     *
//     * @param pageable       Paging.
//     * @param searchTerm     Filter untuk melakukan pencarian lapangan tertentu.
//     * @return               Data semua lapangan.
//     */
//    public Page<LapanganDTO> getAllLapangan(Pageable pageable, String searchTerm) {
//        Page<Lapangan> lapanganPage;
//        if (searchTerm != null && !searchTerm.isEmpty()) {
//            lapanganPage = lapanganRepo.findByNamaLapanganContainingIgnoreCase(searchTerm, pageable);
//        } else {
//            lapanganPage = lapanganRepo.findAll(pageable);
//        }
//        return lapanganPage.map(lapangan -> modelMapper.map(lapangan, LapanganDTO.class));
//    }
//
//    /**
//     * Method untuk Mencari semua nama lapangan dan disimpan ke dalam sebuah list
//     *
//     * @return               List semua nama lapangan.
//     */
//    public List<String> getAllNamaLapangan() {
//        return lapanganRepo.findAll().stream()
//                .map(Lapangan::getNamaLapangan)
//                .collect(Collectors.toList());
//    }
//
//}
//
