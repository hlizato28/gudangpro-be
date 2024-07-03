package co.id.bcafinance.hlbooking.model.projectakhir.pengajuan.cabang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 22/05/2024 17:06
@Last Modified 22/05/2024 17:06
Version 1.0
*/





import co.id.bcafinance.hlbooking.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstPengajuanGudangCabang")
public class PengajuanGudangCabang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPengajuanGudangCabang")
    private Long idPengajuanGudangCabang;

    @ManyToOne
    @JoinColumn(name = "IDUser",foreignKey = @ForeignKey(name = "FK_TO_USER_FROM_PNG_GDG_CBG"))
    private User user;

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

    public Long getIdPengajuanGudangCabang() {
        return idPengajuanGudangCabang;
    }

    public void setIdPengajuanGudangCabang(Long idPengajuanGudangCabang) {
        this.idPengajuanGudangCabang = idPengajuanGudangCabang;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

