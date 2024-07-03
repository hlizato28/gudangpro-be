package co.id.bcafinance.hlbooking.model.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 22/05/2024 09:46
@Last Modified 22/05/2024 09:46
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.Role;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MstMenu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDMenu")
    private Long idMenu;

    @Column(name = "NamaMenu")
    private String namaMenu;

    @ManyToMany
    @JoinTable(name = "MapRoleMenu",
            joinColumns = @JoinColumn(name = "IDRole",
                    foreignKey = @ForeignKey(name = "FK_MAP_TO_ROLE")),
            inverseJoinColumns = @JoinColumn(name = "IDMenu",
                    foreignKey = @ForeignKey(name = "FK_MAP_TO_MENU"))
    )
    private List<Role> ltRole;

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

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public List<Role> getLtRole() {
        return ltRole;
    }

    public void setLtRole(List<Role> ltRole) {
        this.ltRole = ltRole;
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

