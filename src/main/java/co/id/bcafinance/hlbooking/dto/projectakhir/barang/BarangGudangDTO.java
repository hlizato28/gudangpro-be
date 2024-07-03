package co.id.bcafinance.hlbooking.dto.projectakhir.barang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 24/05/2024 11:09
@Last Modified 24/05/2024 11:09
Version 1.0
*/


import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangCabang;

public class BarangGudangDTO {
    private Long idBarangGudang;
    private String namaBarang;
    private String kodeBarang;
    private String satuan;
    private Long jumlah;

    private String kategori;


    public Long getIdBarangGudang() {
        return idBarangGudang;
    }

    public void setIdBarangGudang(Long idBarangGudang) {
        this.idBarangGudang = idBarangGudang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }


}

