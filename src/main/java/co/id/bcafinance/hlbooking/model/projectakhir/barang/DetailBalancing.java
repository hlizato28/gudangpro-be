package co.id.bcafinance.hlbooking.model.projectakhir.barang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/06/2024 11:15
@Last Modified 28/06/2024 11:15
Version 1.0
*/

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstDetailBalancing")
public class DetailBalancing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDetailBalancing")
    private Long idDetailBalancing;

    @ManyToOne
    @JoinColumn(name = "IDBarangGudang", foreignKey = @ForeignKey(name = "FK_FROM_DETAIL_BALANCING_TO_BARANG_GUDANG"))
    private BarangGudang barangGudang;

    @ManyToOne
    @JoinColumn(name = "IDBalancing", foreignKey = @ForeignKey(name = "FK_FROM_DETAIL_BALANCING_TO_BALANCING"))
    private Balancing balancing;

    @Column(name = "StokAwal")
    private Long stokAwal;

    @Column(name = "BarangIn")
    private Long barangIn;

    @Column(name = "BarangOut")
    private Long barangOut;

    @Column(name = "StokAkhir")
    private Long stokAkhir;

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

    public Long getIdDetailBalancing() {
        return idDetailBalancing;
    }

    public void setIdDetailBalancing(Long idDetailBalancing) {
        this.idDetailBalancing = idDetailBalancing;
    }

    public BarangGudang getBarangGudang() {
        return barangGudang;
    }

    public void setBarangGudang(BarangGudang barangGudang) {
        this.barangGudang = barangGudang;
    }

    public Balancing getBalancing() {
        return balancing;
    }

    public void setBalancing(Balancing balancing) {
        this.balancing = balancing;
    }

    public Long getStokAwal() {
        return stokAwal;
    }

    public void setStokAwal(Long stokAwal) {
        this.stokAwal = stokAwal;
    }

    public Long getBarangIn() {
        return barangIn;
    }

    public void setBarangIn(Long barangIn) {
        this.barangIn = barangIn;
    }

    public Long getBarangOut() {
        return barangOut;
    }

    public void setBarangOut(Long barangOut) {
        this.barangOut = barangOut;
    }

    public Long getStokAkhir() {
        return stokAkhir;
    }

    public void setStokAkhir(Long stokAkhir) {
        this.stokAkhir = stokAkhir;
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

