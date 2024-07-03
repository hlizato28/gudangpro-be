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
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Date;
//
//@Entity
//@Table(name = "MstBooking")
//public class Booking {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "IdBooking")
//    private Long idBooking;
//
//    @ManyToOne
//    @JoinColumn(name = "IdLapangan", foreignKey = @ForeignKey(name = "FK_TO_LAPANGAN"))
//    private Lapangan lapangan;
//
//    @ManyToOne
//    @JoinColumn(name = "IdUser", foreignKey = @ForeignKey(name = "FK_TO_USER"))
//    private User user;
//
//
//    @Column(name = "TanggalBooking")
//    private LocalDate tanggalBooking;
//
//    @Column(name = "JamMulaiBooking")
//    private LocalTime jamMulaiBooking;
//
//    @Column(name = "JamSelesaiBooking")
//    private LocalTime jamSelesaiBooking;
//
//    @Column(name = "OrderTime")
//    private LocalTime orderTime;
//
//    @Column(name = "Payment")
//    private Boolean payment;
//
//    @Column(name = "CreatedBy")
//    private String createdBy;
//
//    @Column(name = "CreatedAt")
//    private Date createdAt;
//
//    @Column(name = "UpdatedBy")
//    private String updatedBy;
//
//    @Column(name = "UpdatedAt")
//    private Date updatedAt;
//
//    public Long getIdBooking() {
//        return idBooking;
//    }
//
//    public void setIdBooking(Long idBooking) {
//        this.idBooking = idBooking;
//    }
//
//
//    public Lapangan getLapangan() {
//        return lapangan;
//    }
//
//    public void setLapangan(Lapangan lapangan) {
//        this.lapangan = lapangan;
//    }
//
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//
//    public LocalDate getTanggalBooking() {
//        return tanggalBooking;
//    }
//
//    public void setTanggalBooking(LocalDate tanggalBooking) {
//        this.tanggalBooking = tanggalBooking;
//    }
//
//    public LocalTime getJamMulaiBooking() {
//        return jamMulaiBooking;
//    }
//
//    public void setJamMulaiBooking(LocalTime jamMulaiBooking) {
//        this.jamMulaiBooking = jamMulaiBooking;
//    }
//
//    public LocalTime getJamSelesaiBooking() {
//        return jamSelesaiBooking;
//    }
//
//    public void setJamSelesaiBooking(LocalTime jamSelesaiBooking) {
//        this.jamSelesaiBooking = jamSelesaiBooking;
//    }
//
//    public LocalTime getOrderTime() {
//        return orderTime;
//    }
//
//    public void setOrderTime(LocalTime orderTime) {
//        orderTime = orderTime;
//    }
//
//    public Boolean getPayment() {
//        return payment;
//    }
//
//    public void setPayment(Boolean payment) {
//        this.payment = payment;
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
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
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
