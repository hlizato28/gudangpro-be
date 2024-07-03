package co.id.bcafinance.hlbooking.dto.projectakhir.barang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/06/2024 11:26
@Last Modified 28/06/2024 11:26
Version 1.0
*/

public class DetailBalancingDTO {
    private Long idDetailBalancing;
    private String kodeBarang;
    private String namaBarang;
    private String satuan;
    private Long stokAwal;

    private Long barangIn;

    private Long barangOut;

    private Long stokAkhir;

    public Long getIdDetailBalancing() {
        return idDetailBalancing;
    }

    public void setIdDetailBalancing(Long idDetailBalancing) {
        this.idDetailBalancing = idDetailBalancing;
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

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public Long getStokAwal() {
        return stokAwal;
    }

    public void setStokAwal(Long stokAwal) {
        this.stokAwal = stokAwal;
    }

    public Long getBarangIn() {
        return barangIn;
    }

    public void setBarangIn(Long barangIn) {
        this.barangIn = barangIn;
    }

    public Long getBarangOut() {
        return barangOut;
    }

    public void setBarangOut(Long barangOut) {
        this.barangOut = barangOut;
    }

    public Long getStokAkhir() {
        return stokAkhir;
    }

    public void setStokAkhir(Long stokAkhir) {
        this.stokAkhir = stokAkhir;
    }
}

