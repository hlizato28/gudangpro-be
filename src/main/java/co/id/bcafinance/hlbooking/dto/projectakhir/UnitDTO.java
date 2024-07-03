package co.id.bcafinance.hlbooking.dto.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 24/05/2024 10:20
@Last Modified 24/05/2024 10:20
Version 1.0
*/

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UnitDTO {

    private Long idUnit;

    private String namaUnit;

    private String unitGroup;

    private Boolean isActive;

    public Long getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(Long idUnit) {
        this.idUnit = idUnit;
    }

    public String namaUnit() {
        return namaUnit;
    }

    public String getNamaUnit() {
        return namaUnit;
    }

    public void setNamaUnit(String namaUnit) {
        this.namaUnit = namaUnit;
    }

    public String getUnitGroup() {
        return unitGroup;
    }

    public void setUnitGroup(String unitGroup) {
        this.unitGroup = unitGroup;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}

