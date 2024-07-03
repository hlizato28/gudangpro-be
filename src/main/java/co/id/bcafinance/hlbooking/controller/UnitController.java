package co.id.bcafinance.hlbooking.controller;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 14/06/2024 16:50
@Last Modified 14/06/2024 16:50
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.projectakhir.UnitDTO;
import co.id.bcafinance.hlbooking.service.UnitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/unit")
public class UnitController {
    @Autowired
    UnitService unitService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody UnitDTO unitDTO,
                                         HttpServletRequest request) {
        return unitService.create(unitDTO, request);
    }

//    @GetMapping("/group={group}")
//    public ResponseEntity<Page<UnitDTO>> searchUnitByUnitGroup(
//            @PathVariable("group") String group,
//            @RequestParam(defaultValue = "") String searchTerm,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            HttpServletRequest request) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<UnitDTO> unitDTOPage = unitService.findUnitByUnitGroup(group, pageable, searchTerm, request);
//        return ResponseEntity.ok(unitDTOPage);
//    }

    @GetMapping("/all/with-group")
    public ResponseEntity<Page<UnitDTO>> findAllWithGroup(
            @RequestParam(defaultValue = "") String group,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UnitDTO> unitDTOPage = unitService.findAll(true, group, pageable, searchTerm, request);
        return ResponseEntity.ok(unitDTOPage);
    }

    @GetMapping("/all/no-group")
    public ResponseEntity<Page<UnitDTO>> findAllNoGroup(
            @RequestParam(defaultValue = "") String group,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UnitDTO> unitDTOPage = unitService.findAll(false, group, pageable, searchTerm, request);
        return ResponseEntity.ok(unitDTOPage);
    }

//    @GetMapping("/all/no-group")
//    public ResponseEntity<Page<UnitDTO>> findAllWithoutGroup(
//            @RequestParam(defaultValue = "") String searchTerm,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            HttpServletRequest request) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<UnitDTO> unitDTOPage = unitService.findAllNoGroup(pageable, searchTerm, request);
//        return ResponseEntity.ok(unitDTOPage);
//    }

    @GetMapping("/data/{id}")
    public ResponseEntity<Object> findById( @PathVariable(value = "id") Long id,
                                            HttpServletRequest request){
        return unitService.findById(id,request);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> edit(@PathVariable("id") Long id,
                                       @Valid @RequestBody UnitDTO unitDTO,
                                       HttpServletRequest request) {
        return unitService.edit(id, unitDTO, request);
    }

    @PutMapping("/assign-group/{id}")
    public ResponseEntity<Object> assignGroup(@PathVariable("id") Long id,
                                              @Valid @RequestBody UnitDTO unitDTO,
                                              HttpServletRequest request) {
        return unitService.assignGroup(id, unitDTO, request);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id,
                                         HttpServletRequest request) {
        return unitService.delete(id, request);
    }
}

