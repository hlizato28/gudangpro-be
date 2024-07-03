//package co.id.bcafinance.hlbooking.controller;
///*
//IntelliJ IDEA 2022.3.1 (Community Edition)
//Build #IC-223.8214.52, built on December 20, 2022
//@Author Heinrich a.k.a. Heinrich Lizato
//Java Developer
//Created on 05/05/2024 20:40
//@Last Modified 05/05/2024 20:40
//Version 1.0
//*/
//
//import co.id.bcafinance.hlbooking.dto.LapanganDTO;
//import co.id.bcafinance.hlbooking.model.Lapangan;
//import co.id.bcafinance.hlbooking.service.LapanganService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/adm/l")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
//public class LapanganController {
//    @Autowired
//    LapanganService lapanganService;
//
//    @Autowired
//    ModelMapper modelMapper;
//
//    /**
//     * @desc    Membuat data lapangan baru
//     * @route   POST /api/adm/l/create
//     * @access  Private (Admin)
//     */
//    @PostMapping("/create")
//    public ResponseEntity<Object> createLapangan(@Valid @RequestBody LapanganDTO lapanganDTO, HttpServletRequest request) {
//        Lapangan lapangan = modelMapper.map(lapanganDTO, Lapangan.class);
//        return lapanganService.createLapangan(lapangan, request);
//    }
//
//    /**
//     * @desc    Update data lapangan berdasarkan id lapangan
//     * @route   POST /api/adm/l/edit/:idLapangan
//     * @access  Private (Admin)
//     */
//    @PutMapping("/edit/{id}")
//    public ResponseEntity<Object> editLapangan(@Valid @RequestBody LapanganDTO lapanganDTO,
//                                               @PathVariable(value = "id") Long id,
//                                               HttpServletRequest request){
//
//        Lapangan lapangan = modelMapper.map(lapanganDTO, Lapangan.class);
//        return lapanganService.editLapangan(id,lapanganDTO,request);
//    }
//
//    /**
//     * @desc    Menghapus data lapangan berdasarkan id lapangan
//     * @route   POST /api/adm/l/delete/:idLapangan
//     * @access  Private (Admin)
//     */
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Object> deleteLapangan(@PathVariable(value = "id") Long id,
//                                                 HttpServletRequest request) {
//        return lapanganService.deleteLapangan(id, request);
//    }
//
//    /**
//     * @desc    Mencari data lapangan berdasarkan id lapangan
//     * @route   POST /api/adm/l/data/:idLapangan
//     * @access  Private (Admin)
//     */
//    @GetMapping("/data/{id}")
//    public ResponseEntity<Object> findLapanganById( @PathVariable(value = "id") Long id,
//                                                    HttpServletRequest request){
//        return lapanganService.findById(id,request);
//    }
//
//    /**
//     * @desc    Mencari semua data lapangan
//     * @route   POST /api/adm/l/all
//     * @access  Private (Admin)
//     */
//    @GetMapping("/all")
//    public ResponseEntity<Page<LapanganDTO>> getAllLapangan(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "6") int size,
//            @RequestParam(defaultValue = "") String searchTerm) {
//        Page<LapanganDTO> lapanganDTOPage = lapanganService.getAllLapangan(PageRequest.of(page, size), searchTerm);
//        return ResponseEntity.ok(lapanganDTOPage);
//    }
//
//    /**
//     * @desc    Mencari semua nama lapangan dan disimpan ke dalam sebuah list
//     * @route   POST /api/adm/l/list
//     * @access  Private (Admin)
//     */
//    @GetMapping("/list")
//    public ResponseEntity<List<String>> getAllLapanganNames() {
//        List<String> lapanganNames = lapanganService.getAllNamaLapangan();
//        return ResponseEntity.ok(lapanganNames);
//    }
//
//}
//
