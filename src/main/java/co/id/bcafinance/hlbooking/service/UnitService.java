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
import co.id.bcafinance.hlbooking.handler.RequestCapture;
import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import co.id.bcafinance.hlbooking.model.Role;
import co.id.bcafinance.hlbooking.model.UnitGroup;
import co.id.bcafinance.hlbooking.model.projectakhir.Unit;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UnitService {
    @Autowired
    UnitRepo unitRepo;
    @Autowired
    private ModulAuthority modulAuthority;
    @Autowired
    UnitGroupRepo unitGroupRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GlobalFunction globalFunction;

    private Map<String, Object> mapToken = new HashMap<>();

    private String[] strExceptionArr = new String[2];

    public ResponseEntity<Object> create(UnitDTO unitDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        String namaUnit = unitDTO.getNamaUnit();
        if (unitRepo.existsByNamaUnitAndIsActive(namaUnit, true)) {
            return new ResponseHandler().generateResponse("Nama Unit sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04035", request);
        }

        if (!unitDTO.getUnitGroup().isEmpty()) {
            Optional<UnitGroup> optionalUnitGroup = unitGroupRepo.findByNamaUnitGroupAndIsActive(unitDTO.getUnitGroup(), true);
            if (optionalUnitGroup.isEmpty()) {
                return globalFunction.dataNotFoundById(request);
            }
        }

        try {
            Unit unit = new Unit();
            unit.setNamaUnit(unitDTO.getNamaUnit());
            unit.setActive(true);
            unit.setCreatedBy(Long.parseLong(mapToken.get("de").toString()));

            if (!unitDTO.getUnitGroup().isEmpty()) {
                Optional<UnitGroup> optionalUnitGroup = unitGroupRepo.findByNamaUnitGroupAndIsActive(unitDTO.getUnitGroup(), true);
                if (optionalUnitGroup.isEmpty()) {
                    return globalFunction.dataNotFoundById(request);
                }

                UnitGroup ug = optionalUnitGroup.get();

                unit.setUnitGroup(ug);
            }

            unitRepo.save(unit);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseHandler().generateResponse("Unit Gagal Dibuat!!",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04032", request);
        }

        return new ResponseHandler().generateResponse("Unit Berhasil Dibuat!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> edit(Long id, UnitDTO unitDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Unit> optionalUnit = unitRepo.findById(id);
        if (optionalUnit.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Unit u = optionalUnit.get();

        String namaUnit = unitDTO.getNamaUnit();
        if (!u.getNamaUnit().equals(namaUnit) && unitRepo.existsByNamaUnitAndIsActive(namaUnit, true)) {
            return new ResponseHandler().generateResponse("Nama Unit sudah digunakan!!",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE04036", request);
        }

        try {
            u.setNamaUnit(unitDTO.getNamaUnit());
            u.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            u.setUpdatedAt(new Date());

            if (!unitDTO.getUnitGroup().isEmpty()) {
                Optional<UnitGroup> optionalUnitGroup = unitGroupRepo.findByNamaUnitGroupAndIsActive(unitDTO.getUnitGroup(), true);
                if (optionalUnitGroup.isEmpty()) {
                    return globalFunction.dataNotFoundById(request);
                }

                UnitGroup ug = optionalUnitGroup.get();

                u.setUnitGroup(ug);
            }

            unitRepo.save(u);
        } catch (Exception e) {
            strExceptionArr[1] = "edit(Long id, UnitDTO unitDTO, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Unit Gagal Diubah !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04022", request);
        }
        return new ResponseHandler().generateResponse("Data Unit Berhasil Diubah!!",
                HttpStatus.OK,
                null,
                null, request);
    }

    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Unit> optionalUnit = unitRepo.findById(id);
        if (optionalUnit.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Unit u = optionalUnit.get();

        try {
            u.setActive(false);
            u.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            u.setUpdatedAt(new Date());

            unitRepo.save(u);
        } catch (Exception e) {
            strExceptionArr[1] = "doDelete(Long id, HttpServletRequest request) - Barang" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Unit Gagal Dihapus !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04024", request);
        }
        return new ResponseHandler().generateResponse("Data Unit Berhasil Dihapus!!",
                HttpStatus.OK,
                null,
                null, request);
    }

//    public Page<UnitDTO> findUnitByUnitGroup(String unitGroup, Pageable pageable, String searchTerm, HttpServletRequest request) {
//        Optional<UnitGroup> optionalUnitGroup = unitGroupRepo.findByNamaUnitGroupAndIsActive(unitGroup, true);
//        if (optionalUnitGroup.isEmpty()) {
//            return Page.empty();
//        }
//
//        UnitGroup ug = optionalUnitGroup.get();
//
//        Page<Unit> unitPage;
//        unitPage = unitRepo.findByUnitGroupAndIsActiveAndUnitOrUnitGroupContainingIgnoreCase(ug, true, searchTerm, pageable);
//
//
//        return unitPage.map(unit -> modelMapper.map(unit, UnitDTO.class));
//    }

    public Page<UnitDTO> findAll(Boolean withGroup, String group, Pageable pageable, String searchTerm, HttpServletRequest request) {
        Page<Unit> unitPage;

        UnitGroup ug = null;

        if (withGroup) {
            if (group != null && !group.trim().isEmpty()) {
                Optional<UnitGroup> optionalUnitGroup = unitGroupRepo.findByNamaUnitGroupAndIsActive(group, true);
                if (optionalUnitGroup.isEmpty()) {
                    return Page.empty();
                }

                ug = optionalUnitGroup.get();
            }

            unitPage = unitRepo.findAllWithGroup(true, ug, searchTerm, pageable);
        } else {
            unitPage = unitRepo.findAllWithoutGroup(true, searchTerm, pageable);
        }

        return unitPage.map(unit -> modelMapper.map(unit, UnitDTO.class));
    }

//    public Page<UnitDTO> findAllNoGroup(Pageable pageable, String searchTerm, HttpServletRequest request) {
//        Page<Unit> unitPage;
//        unitPage = unitRepo.findAllUnitWithoutGroupBySearchTerm(true, searchTerm, pageable);
//
//
//        return unitPage.map(unit -> modelMapper.map(unit, UnitDTO.class));
//    }

    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<Unit> optionalUnit = unitRepo.findById(id);
        if (optionalUnit.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Unit u = optionalUnit.get();

        UnitDTO unitDTO = modelMapper.map(u, UnitDTO.class);

        return new ResponseHandler().generateResponse("Data Ditemukan!!",
                HttpStatus.OK,
                unitDTO,
                null, request);
    }

    public ResponseEntity<Object> assignGroup(Long id, UnitDTO unitDTO, HttpServletRequest request) {
        mapToken = modulAuthority.checkAuthorization(request);

        Optional<Unit> optionalUnit = unitRepo.findById(id);
        if (optionalUnit.isEmpty()) {
            return globalFunction.dataNotFoundById(request);
        }

        Unit u = optionalUnit.get();

        try {
            if(!unitDTO.getUnitGroup().isEmpty()){
                Optional<UnitGroup> optionalUnitGroup = unitGroupRepo.findByNamaUnitGroupAndIsActive(unitDTO.getUnitGroup(), true);

                UnitGroup ug = optionalUnitGroup.get();

                u.setUnitGroup(ug);
            } else {
                return new ResponseHandler().generateResponse("Unit Group Belum Dipilih! ",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        "FE04024", request);
            }

            u.setUpdatedBy(Long.parseLong(mapToken.get("de").toString()));
            u.setUpdatedAt(new Date());

            unitRepo.save(u);
        } catch (Exception e) {
            strExceptionArr[1] = "edit(Long id, UnitDTO unitDTO, HttpServletRequest request)" + RequestCapture.allRequest(request);
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());

            return new ResponseHandler().generateResponse("Data Unit Gagal Diubah !! ",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE04022", request);
        }
        return new ResponseHandler().generateResponse("Data Unit Berhasil Diubah!!",
                HttpStatus.OK,
                null,
                null, request);
    }
}

