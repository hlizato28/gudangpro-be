package co.id.bcafinance.hlbooking.dto.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 16/06/2024 22:07
@Last Modified 16/06/2024 22:07
Version 1.0
*/

public class UnitGroupDTO {
    private Long idUnitGroup;

    private String namaUnitGroup;

    private Boolean isActive;

    public Long getIdUnitGroup() {
        return idUnitGroup;
    }

    public void setIdUnitGroup(Long idUnitGroup) {
        this.idUnitGroup = idUnitGroup;
    }

    public String getNamaUnitGroup() {
        return namaUnitGroup;
    }

    public void setNamaUnitGroup(String namaUnitGroup) {
        this.namaUnitGroup = namaUnitGroup;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}

