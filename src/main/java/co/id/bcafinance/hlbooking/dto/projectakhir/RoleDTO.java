package co.id.bcafinance.hlbooking.dto.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 24/05/2024 10:41
@Last Modified 24/05/2024 10:41
Version 1.0
*/

import java.util.List;

public class RoleDTO {

    private Long idRole;

    private String namaRole;

    private List<MenuDTO> ltMenu;

    private Boolean isActive;

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }

    public String getNamaRole() {
        return namaRole;
    }

    public void setNamaRole(String namaRole) {
        this.namaRole = namaRole;
    }

    public List<MenuDTO> getLtMenu() {
        return ltMenu;
    }

    public void setLtMenu(List<MenuDTO> ltMenu) {
        this.ltMenu = ltMenu;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}

