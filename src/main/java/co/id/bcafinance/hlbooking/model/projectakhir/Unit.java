package co.id.bcafinance.hlbooking.model.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:41
@Last Modified 05/05/2024 21:41
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.UnitGroup;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstUnit")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUnit")
    private Long idUnit;

    @Column(name = "NamaUnit")
    private String namaUnit;

    @ManyToOne
    @JoinColumn(name = "IDUnitGroup",foreignKey = @ForeignKey(name = "FK_TO_UNIT_GROUP_FROM_USR"))
    private UnitGroup unitGroup;

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

    public Long getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(Long idUnit) {
        this.idUnit = idUnit;
    }

    public String getNamaUnit() {
        return namaUnit;
    }

    public void setNamaUnit(String namaUnit) {
        this.namaUnit = namaUnit;
    }

    public UnitGroup getUnitGroup() {
        return unitGroup;
    }

    public void setUnitGroup(UnitGroup unitGroup) {
        this.unitGroup = unitGroup;
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

