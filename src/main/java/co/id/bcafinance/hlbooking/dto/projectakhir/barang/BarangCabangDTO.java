package co.id.bcafinance.hlbooking.dto.projectakhir.barang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 24/05/2024 10:58
@Last Modified 24/05/2024 10:58
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.Cabang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;

import java.math.BigDecimal;

public class BarangCabangDTO {

    private Long idBarangCabang;
    private String kodeBarang;
    private String namaBarang;
    private String cabang;

    private String kategoriBarang;

//    private BigDecimal harga;

    //private Boolean isActive;

    public Long getIdBarangCabang() {
        return idBarangCabang;
    }

    public void setIdBarangCabang(Long idBarangCabang) {
        this.idBarangCabang = idBarangCabang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getKategoriBarang() {
        return kategoriBarang;
    }

    public void setKategoriBarang(String kategoriBarang) {
        this.kategoriBarang = kategoriBarang;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }
    //    public BigDecimal getHarga() {
//        return harga;
//    }
//
//    public void setHarga(BigDecimal harga) {
//        this.harga = harga;
//    }

//    public Boolean getIsActive() {
//        return isActive;
//    }
//
//    public void setActive(Boolean active) {
//        isActive = active;
//    }//
}

