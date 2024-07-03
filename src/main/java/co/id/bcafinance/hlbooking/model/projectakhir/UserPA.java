//package co.id.bcafinance.hlbooking.model.projectakhir;
///*
//IntelliJ IDEA 2022.3.1 (Community Edition)
//Build #IC-223.8214.52, built on December 20, 2022
//@Author Heinrich a.k.a. Heinrich Lizato
//Java Developer
//Created on 05/05/2024 21:42
//@Last Modified 05/05/2024 21:42
//Version 1.0
//*/
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//@Table(name = "MstUserPA")
//public class UserPA {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "IDUser")
//    private Long idUser;
//
//    @Column(name = "Username",unique = true, nullable = false)
//    private String username;
//
//    @ManyToOne
//    @JoinColumn(name = "IDRole",foreignKey = @ForeignKey(name = "FK_TO_ROLE"))
//    private RolePA rolePA;
//
//    @Column(name = "Token")
//    private String token;
//
//    @ManyToOne
//    @JoinColumn(name = "IDCabang",foreignKey = @ForeignKey(name = "FK_TO_CABANG"))
//    private Cabang cabang;
//
//    @ManyToOne
//    @JoinColumn(name = "IDUnit",foreignKey = @ForeignKey(name = "FK_TO_UNIT"))
//    private Unit unit;
//
//    @Column(name = "IsActive")
//    private Boolean isActive;
//
//    @Column(name = "CreatedBy")
//    private Long createdBy = 1L;
//
//    @Column(name = "CreatedAt")
//    private Date createdAt = new Date();
//
//    @Column(name = "UpdatedBy")
//    private Long updatedBy;
//
//    @Column(name = "UpdatedAt")
//    private Date updatedAt;
//
//    public Long getIdUser() {
//        return idUser;
//    }
//
//    public void setIdUser(Long idUser) {
//        this.idUser = idUser;
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
//    public RolePA getRole() {
//        return rolePA;
//    }
//
//    public void setRole(RolePA rolePA) {
//        this.rolePA = rolePA;
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
//    public Boolean getIsActive() {
//        return isActive;
//    }
//
//    public void setActive(Boolean active) {
//        isActive = active;
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
//}
//
