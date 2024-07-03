package co.id.bcafinance.hlbooking.dto.projectakhir.pengajuan;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 24/05/2024 11:25
@Last Modified 24/05/2024 11:25
Version 1.0
*/



import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangCabang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;

import java.util.List;

public class PengajuanGudangCabangDTO {
    private Long idPengajuanGudangCabang;
    private String user;

    private List<DetailPengajuanGudangCabangDTO> details;

    public List<DetailPengajuanGudangCabangDTO> getDetails() {
        return details;
    }

    public void setDetails(List<DetailPengajuanGudangCabangDTO> details) {
        this.details = details;
    }

    public Long getIdPengajuanGudangCabang() {
        return idPengajuanGudangCabang;
    }

    public void setIdPengajuanGudangCabang(Long idPengajuanGudangCabang) {
        this.idPengajuanGudangCabang = idPengajuanGudangCabang;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}

