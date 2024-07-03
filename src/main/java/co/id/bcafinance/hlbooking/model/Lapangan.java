//package co.id.bcafinance.hlbooking.model;
///*
//IntelliJ IDEA 2022.3.1 (Community Edition)
//Build #IC-223.8214.52, built on December 20, 2022
//@Author Heinrich a.k.a. Heinrich Lizato
//Java Developer
//Created on 05/05/2024 21:41
//@Last Modified 05/05/2024 21:41
//Version 1.0
//*/
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Date;
//
//@Entity
//@Table(name = "MstLapangan")
//public class Lapangan {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "IdLapangan")
//    private Long idLapangan;
//
//    @Column(name = "NamaLapangan")
//    private String namaLapangan;
//
//    @Column(name = "JamMulai")
//    private LocalTime jamMulai;
//
//    @Column(name = "JamSelesai")
//    private LocalTime jamSelesai;
//
//    @Column(name = "CreatedBy")
//    private String createdBy;
//
//    @Column(name = "CreatedAt")
//    private LocalDateTime createdAt;
//
//    @Column(name = "UpdatedBy")
//    private String updatedBy;
//
//    @Column(name = "UpdatedAt")
//    private Date updatedAt;
//
//    public LocalTime getJamMulai() {
//        return jamMulai;
//    }
//
//    public void setJamMulai(LocalTime jamMulai) {
//        this.jamMulai = jamMulai;
//    }
//
//    public LocalTime getJamSelesai() {
//        return jamSelesai;
//    }
//
//    public void setJamSelesai(LocalTime jamSelesai) {
//        this.jamSelesai = jamSelesai;
//    }
//
//    public Long getIdLapangan() {
//        return idLapangan;
//    }
//
//    public void setIdLapangan(Long idLapangan) {
//        this.idLapangan = idLapangan;
//    }
//
//    public String getNamaLapangan() {
//        return namaLapangan;
//    }
//
//    public void setNamaLapangan(String namaLapangan) {
//        this.namaLapangan = namaLapangan;
//    }
//
//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getUpdatedBy() {
//        return updatedBy;
//    }
//
//    public void setUpdatedBy(String updatedBy) {
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
