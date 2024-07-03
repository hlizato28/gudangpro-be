package co.id.bcafinance.hlbooking.dto.projectakhir.barang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 01/07/2024 16:54
@Last Modified 01/07/2024 16:54
Version 1.0
*/

public class ReportBalancingDTO {
    private Long idDetailBalancing;
    private String  kodeBarang;
    private String namaBarang;
    private Long stokAwal;
    private Long stokAhkhir;
    private Long barangIn;
    private Long barangOut;
    private Long opr;
    private Long uc;
    private Long nc;
    private Long kkb;
    private Long ds;
    private Long asr;

    public Long getIdDetailBalancing() {
        return idDetailBalancing;
    }

    public void setIdDetailBalancing(Long idDetailBalancing) {
        this.idDetailBalancing = idDetailBalancing;
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

    public Long getStokAwal() {
        return stokAwal;
    }

    public void setStokAwal(Long stokAwal) {
        this.stokAwal = stokAwal;
    }

    public Long getStokAhkhir() {
        return stokAhkhir;
    }

    public void setStokAhkhir(Long stokAhkhir) {
        this.stokAhkhir = stokAhkhir;
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

    public Long getOpr() {
        return opr;
    }

    public void setOpr(Long opr) {
        this.opr = opr;
    }

    public Long getUc() {
        return uc;
    }

    public void setUc(Long uc) {
        this.uc = uc;
    }

    public Long getNc() {
        return nc;
    }

    public void setNc(Long nc) {
        this.nc = nc;
    }

    public Long getKkb() {
        return kkb;
    }

    public void setKkb(Long kkb) {
        this.kkb = kkb;
    }

    public Long getDs() {
        return ds;
    }

    public void setDs(Long ds) {
        this.ds = ds;
    }

    public Long getAsr() {
        return asr;
    }

    public void setAsr(Long asr) {
        this.asr = asr;
    }
}

