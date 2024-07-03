package co.id.bcafinance.hlbooking.service;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 13/06/2024 17:07
@Last Modified 13/06/2024 17:07
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.OtherConfig;
import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
import co.id.bcafinance.hlbooking.dto.projectakhir.UnitDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.UnitGroupDTO;
import co.id.bcafinance.hlbooking.handler.RequestCapture;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.UnitGroup;
import co.id.bcafinance.hlbooking.model.projectakhir.Unit;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import co.id.bcafinance.hlbooking.repo.UnitGroupRepo;
import co.id.bcafinance.hlbooking.repo.projectakhir.UnitRepo;
import co.id.bcafinance.hlbooking.util.GlobalFunction;
import co.id.bcafinance.hlbooking.util.LoggingFile;
import org.modelmapper.ModelMapper;
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
public class UnitGroupService {
    @Autowired
    UnitGroupRepo unitGroupRepo;
    @Autowired
    private ModulAuthority modulAuthority;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GlobalFunction globalFunction;

    private Map<String, Object> mapToken = new HashMap<>();

    private String[] strExceptionArr = new String[2];

    public ResponseEntity<Object> create(UnitGroupDTO unitGroupDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        String namaUnitGroup = unitGroupDTO.getNamaUnitGroup();
        if (unitGroupRepo.existsByNamaUnitGroupAndIsActive(namaUnitGroup, true)) {
            return new ResponseHandler().generateResponse("Nama Unit Group sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04035", request);
        }

        try {
            UnitGroup unitGroup = new UnitGroup();
            unitGroup.setNamaUnitGroup(unitGroupDTO.getNamaUnitGroup());
            unitGroup.setActive(true);
            unitGroup.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));

            unitGroupRepo.save(unitGroup);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHandler().generateResponse("Unit Group Gagal Dibuat!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04032", request);
        }

        return new ResponseHandler().generateResponse("Unit Group Berhasil Dibuat!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> edit(Long id, UnitGroupDTO unitGroupDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<UnitGroup> optionalUnitGroup = unitGroupRepo.findById(id);
        if (optionalUnitGroup.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        UnitGroup ug = optionalUnitGroup.get();

        String namaUnitGroup = unitGroupDTO.getNamaUnitGroup();
        if (!ug.getNamaUnitGroup().equals(namaUnitGroup) && unitGroupRepo.existsByNamaUnitGroupAndIsActive(namaUnitGroup, true)) {
            return new ResponseHandler().generateResponse("Nama Unit Group sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04036", request);
        }

        try {
            ug.setNamaUnitGroup(unitGroupDTO.getNamaUnitGroup());
            ug.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            ug.setUpdatedAt(new Date());

            unitGroupRepo.save(ug);
        } catch (Exception e) {
            strExceptionArr[1] = "edit(Long id, UnitDTO unitDTO, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Unit Group Gagal Diubah !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04022", request);
        }
        return new ResponseHandler().generateResponse("Data Unit Group Berhasil Diubah!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<UnitGroup> optionalUnitGroup = unitGroupRepo.findById(id);
        if (optionalUnitGroup.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        UnitGroup ug = optionalUnitGroup.get();

        try {
            ug.setActive(false);
            ug.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            ug.setUpdatedAt(new Date());

            unitGroupRepo.save(ug);
        } catch (Exception e) {
            strExceptionArr[1] = "doDelete(Long id, HttpServletRequest request) - Barang" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Unit Group Gagal Dihapus !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04024", request);
        }
        return new ResponseHandler().generateResponse("Data Unit Group Berhasil Dihapus!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public Page<UnitGroupDTO> findAll(Pageable pageable, String searchTerm, HttpServletRequest request) {
        Page<UnitGroup> unitGroupPage;
        unitGroupPage = unitGroupRepo.findAllUnitGroupBySearchTerm(true, searchTerm, pageable);


        return unitGroupPage.map(unitGroup -> modelMapper.map(unitGroup, UnitGroupDTO.class));
    }

    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<UnitGroup> optionalUnitGroup = unitGroupRepo.findById(id);
        if (optionalUnitGroup.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        UnitGroup ug = optionalUnitGroup.get();

        UnitGroupDTO unitGroupDTO = modelMapper.map(ug, UnitGroupDTO.class);

        return new ResponseHandler().generateResponse("Data Ditemukan!!",
                HttpStatus.OK,
                unitGroupDTO,
                null, request);
    }

    public ResponseEntity<Object> listAll(HttpServletRequest request) {
        try {
            List<String> unitGroup = unitGroupRepo.findAllByIsActiveOrderByNamaUnitGroupAsc(true).stream()
                    .map(UnitGroup::getNamaUnitGroup)
                    .collect(Collectors.toList());
            return new ResponseHandler().generateResponse("Data Unit Group Berhasil Ditemukan!!",
                    HttpStatus.OK,
                    unitGroup,
                    null,
                    request);
        } catch (Exception e) {
            return new ResponseHandler().generateResponse("Data Unit Group Gagal Ditemukan!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04026",
                    request);
        }
    }
}

