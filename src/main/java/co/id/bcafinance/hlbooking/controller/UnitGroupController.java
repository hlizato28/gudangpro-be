package co.id.bcafinance.hlbooking.controller;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 16/06/2024 22:04
@Last Modified 16/06/2024 22:04
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.projectakhir.UnitDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.UnitGroupDTO;
import co.id.bcafinance.hlbooking.service.UnitGroupService;
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
@RequestMapping("/api/unit-group")
public class UnitGroupController {
    @Autowired
    UnitGroupService unitGroupService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody UnitGroupDTO unitGroupDTO,
                                         HttpServletRequest request) {
        return unitGroupService.create(unitGroupDTO, request);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<UnitGroupDTO>> findAll(
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UnitGroupDTO> unitGroupDTOPage = unitGroupService.findAll(pageable, searchTerm, request);
        return ResponseEntity.ok(unitGroupDTOPage);
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<Object> findById( @PathVariable(value = "id") Long id,
                                            HttpServletRequest request){
        return unitGroupService.findById(id,request);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> edit(@PathVariable("id") Long id,
                                       @Valid @RequestBody UnitGroupDTO unitGroupDTO,
                                       HttpServletRequest request) {
        return unitGroupService.edit(id, unitGroupDTO, request);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id,
                                         HttpServletRequest request) {
        return unitGroupService.delete(id, request);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listAll(HttpServletRequest request) {
        return unitGroupService.listAll(request);
    }
}

