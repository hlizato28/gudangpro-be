package co.id.bcafinance.hlbooking.dto.projectakhir.pengajuan;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 27/05/2024 09:39
@Last Modified 27/05/2024 09:39
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangGudang;

import java.util.Date;

public class DetailPengajuanGudangCabangDTO {
    private Long idDetailPengajuanGudangCabang;
    private String kodeBarang;
    private String namaBarang;
    private String satuan;

    private Long stok;
    private Long jumlahDiminta;
    private Long jumlahApproved;
    private Long jumlahDiterima;

    public Long getIdDetailPengajuanGudangCabang() {
        return idDetailPengajuanGudangCabang;
    }

    public void setIdDetailPengajuanGudangCabang(Long idDetailPengajuanGudangCabang) {
        this.idDetailPengajuanGudangCabang = idDetailPengajuanGudangCabang;
    }

    public Long getStok() {
        return stok;
    }

    public void setStok(Long stok) {
        this.stok = stok;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
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


}

