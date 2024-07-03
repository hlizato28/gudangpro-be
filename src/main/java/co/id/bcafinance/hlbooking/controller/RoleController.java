package co.id.bcafinance.hlbooking.controller;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 20/06/2024 10:07
@Last Modified 20/06/2024 10:07
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.projectakhir.RoleDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.KategoriBarangDTO;
import co.id.bcafinance.hlbooking.service.RoleService;
import co.id.bcafinance.hlbooking.service.projectakhir.KategoriBarangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody RoleDTO roleDTO,
                                         HttpServletRequest request) {
        return roleService.create(roleDTO, request);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> edit(@PathVariable("id") Long id,
                                       @Valid @RequestBody RoleDTO roleDTO,
                                       HttpServletRequest request) {
        return roleService.edit(id, roleDTO, request);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id,
                                         HttpServletRequest request) {
        return roleService.delete(id, request);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllKategori(HttpServletRequest request) {
        return roleService.getListRole(request);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<RoleDTO>> getAllBarang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm) {
        Page<RoleDTO> roleDTOPage = roleService.findAllRole(PageRequest.of(page, size), searchTerm);
        return ResponseEntity.ok(roleDTOPage);
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<Object> findById( @PathVariable(value = "id") Long id,
                                            HttpServletRequest request){
        return roleService.findById(id,request);
    }
}

