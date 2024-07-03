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

import co.id.bcafinance.hlbooking.model.projectakhir.Unit;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUser")
    private Long idUser;

    @Column(name = "Username",unique = true, nullable = false)
    private String username;

    @Column(name = "Nama")
    private String nama;

    @ManyToOne
    @JoinColumn(name = "IDRole",foreignKey = @ForeignKey(name = "FK_TO_ROLE"))
    private Role role;

    @Column(name = "Cabang")
    private String cabang;

    @Column(name = "Jabatan")
    private String jabatan;

    @ManyToOne
    @JoinColumn(name = "IDUnit",foreignKey = @ForeignKey(name = "FK_TO_UNIT_FROM_USR"))
    private Unit unit;

    @Column(name = "IsApproved")
    private Boolean isApproved;

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

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean approved) {
        isApproved = approved;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "IdUser")
//    private Long idUser;
//
//    @Column(name = "Email",unique = true, nullable = false)
//    private String email;
//
//    @Column(name = "Username",unique = true, nullable = false)
//    private String username;
//
//    @Column(name = "Password", nullable = false)
//    private String password;
//
//    @Column(name = "Nama")
//    private String nama;
//
//    @Column(name = "NoTelepon",unique = true, nullable = false)
//    private String noTelepon;
//
//    @ManyToOne
//    @JoinColumn(name = "IDCabang",foreignKey = @ForeignKey(name = "FK_TO_CABANG"))
//    private Cabang cabang;
//
//    @ManyToOne
//    @JoinColumn(name = "IDUnit",foreignKey = @ForeignKey(name = "FK_TO_UNIT"))
//    private Unit unit;
//
//    @ManyToOne
//    @JoinColumn(name = "IdRole",foreignKey = @ForeignKey(name = "FK_TO_ROLE"))
//    private Role role;
//
//    @Column(name = "Token")
//    private String token;
//
//    @Column(name = "IsRegistered")
//    private Boolean isRegistered;
//
//    @Column(name = "CreatedBy", nullable = false)
//    private Long createdBy = 1L;
//
//    @Column(name = "CreatedAt", columnDefinition = "Datetime default GETDATE()")
//    private Date createdAt;
//
//    @Column(name = "UpdatedBy")
//    private Long updatedBy;
//
//    @Column(name = "UpdatedAt", columnDefinition = "Datetime")
//    private Date updatedAt;
//
//    public Cabang getCabang() {
//        return cabang;
//    }
//
//    public void setCabang(Cabang cabang) {
//        this.cabang = cabang;
//    }
//
//    public Unit getUnit() {
//        return unit;
//    }
//
//    public void setUnit(Unit unit) {
//        this.unit = unit;
//    }
//
//    public Long getIdUser() {
//        return idUser;
//    }
//
//    public void setIdUser(Long idUser) {
//        this.idUser = idUser;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getNama() {
//        return nama;
//    }
//
//    public void setNama(String nama) {
//        this.nama = nama;
//    }
//
//    public String getNoTelepon() {
//        return noTelepon;
//    }
//
//    public void setNoTelepon(String noTelepon) {
//        this.noTelepon = noTelepon;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public Boolean getRegistered() {
//        return isRegistered;
//    }
//
//    public void setRegistered(Boolean registered) {
//        isRegistered = registered;
//    }
//
//    public Long getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(Long createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public Long getUpdatedBy() {
//        return updatedBy;
//    }
//
//    public void setUpdatedBy(Long updatedBy) {
//        this.updatedBy = updatedBy;
//    }
//
//    public Date getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(Date updatedAt) {
//        this.updatedAt = updatedAt;
//    }
}

