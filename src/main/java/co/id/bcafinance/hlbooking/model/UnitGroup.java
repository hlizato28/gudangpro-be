package co.id.bcafinance.hlbooking.model;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 13/06/2024 15:27
@Last Modified 13/06/2024 15:27
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.Unit;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstUnitGroup")
public class UnitGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUnitGroup")
    private Long idUnitGroup;

    @Column(name = "NamaUnitGroup")
    private String namaUnitGroup;

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

