package co.id.bcafinance.hlbooking.model;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:42
@Last Modified 05/05/2024 21:42
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.Menu;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="MstRole")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRole")
    private Long idRole;

    @Column(name = "NamaRole")
    private String namaRole;

    @ManyToMany(mappedBy = "ltRole")
    private List<Menu> ltMenu;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "CreatedBy")
    private Long createdBy = 1L;

    @Column(name = "CreatedAt")
    private Date createdAt = new Date();

    @Column(name = "UpdatedBy")
    private Long updatedBy;

    @Column(name = "UpdatedAt")
    private Date updatedAt;


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

    public List<Menu> getLtMenu() {
        return ltMenu;
    }

    public void setLtMenu(List<Menu> ltMenu) {
        this.ltMenu = ltMenu;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

