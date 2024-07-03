package co.id.bcafinance.hlbooking.model.projectakhir.barang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 22/05/2024 14:26
@Last Modified 22/05/2024 14:26
Version 1.0
*/

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstBarangGudang")
public class BarangGudang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDBarangGudang")
    private Long idBarangGudang;

    @ManyToOne
    @JoinColumn(name = "IDBarangCabang",foreignKey = @ForeignKey(name = "FK_TO_BARANG_CABANG"))
    private BarangCabang barangCabang;

    @Column(name = "Jumlah")
    private Long jumlah;


    @Column(name = "CreateApproved")
    private Boolean createApproved;

    @Column(name = "DeleteToBeApprove")
    private Boolean deleteToBeApprove;

    @Column(name = "IsActive")
    private Boolean isActive = true;

    @Column(name = "CreatedBy")
    private Long createdBy = 1L;

    @Column(name = "CreatedAt")
    private Date createdAt = new Date();

    @Column(name = "UpdatedBy")
    private Long updatedBy;

    @Column(name = "UpdatedAt")
    private Date updatedAt;

    public Long getIdBarangGudang() {
        return idBarangGudang;
    }

    public void setIdBarangGudang(Long idBarangGudang) {
        this.idBarangGudang = idBarangGudang;
    }

    public BarangCabang getBarangCabang() {
        return barangCabang;
    }

    public void setBarangCabang(BarangCabang barangCabang) {
        this.barangCabang = barangCabang;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }


    public Boolean getCreateApproved() {
        return createApproved;
    }

    public void setCreateApproved(Boolean createApproved) {
        this.createApproved = createApproved;
    }

    public Boolean getDeleteToBeApprove() {
        return deleteToBeApprove;
    }

    public void setDeleteToBeApprove(Boolean deleteToBeApprove) {
        this.deleteToBeApprove = deleteToBeApprove;
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

