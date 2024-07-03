package co.id.bcafinance.hlbooking.model.projectakhir.pengajuan.cabang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 24/05/2024 08:53
@Last Modified 24/05/2024 08:53
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangGudang;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstDetailPengajuanGudangCabang")
public class DetailPengajuanGudangCabang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDetailPengajuanGudangCabang")
    private Long idDetailPengajuanGudangCabang;

    @ManyToOne
    @JoinColumn(name = "IDPengajuanGudangCabang",foreignKey = @ForeignKey(name = "FK_TO_PENGAJUAN_GUDANG_CABANG"))
    private PengajuanGudangCabang pengajuanGudangCabang;

    @ManyToOne
    @JoinColumn(name = "IDBarangGudang",foreignKey = @ForeignKey(name = "FK_TO_BARANG_GUDANG"))
    private BarangGudang barangGudang;

    @Column(name = "JumlahDiminta")
    private Long jumlahDiminta;

    @Column(name = "JumlahApproved")
    private Long jumlahApproved;

    @Column(name = "JumlahDiterima")
    private Long jumlahDiterima;

    @Column(name = "IsApproved")
    private Boolean isApproved;

    @Column(name = "IsDiterima")
    private Boolean isDiterima;


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

    public Long getIdDetailPengajuanGudangCabang() {
        return idDetailPengajuanGudangCabang;
    }

    public void setIdDetailPengajuanGudangCabang(Long idDetailPengajuanGudangCabang) {
        this.idDetailPengajuanGudangCabang = idDetailPengajuanGudangCabang;
    }

    public PengajuanGudangCabang getPengajuanGudangCabang() {
        return pengajuanGudangCabang;
    }

    public void setPengajuanGudangCabang(PengajuanGudangCabang pengajuanGudangCabang) {
        this.pengajuanGudangCabang = pengajuanGudangCabang;
    }

    public BarangGudang getBarangGudang() {
        return barangGudang;
    }

    public void setBarangGudang(BarangGudang barangGudang) {
        this.barangGudang = barangGudang;
    }

    public Long getJumlahDiminta() {
        return jumlahDiminta;
    }

    public void setJumlahDiminta(Long jumlahDiminta) {
        this.jumlahDiminta = jumlahDiminta;
    }

    public Long getJumlahApproved() {
        return jumlahApproved;
    }

    public void setJumlahApproved(Long jumlahApproved) {
        this.jumlahApproved = jumlahApproved;
    }


    public Long getJumlahDiterima() {
        return jumlahDiterima;
    }

    public void setJumlahDiterima(Long jumlahDiterima) {
        this.jumlahDiterima = jumlahDiterima;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Boolean getIsDiterima() {
        return isDiterima;
    }

    public void setDiterima(Boolean diterima) {
        isDiterima = diterima;
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

