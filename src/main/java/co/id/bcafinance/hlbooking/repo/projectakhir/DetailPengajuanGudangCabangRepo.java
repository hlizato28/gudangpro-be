package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 27/06/2024 08:43
@Last Modified 27/06/2024 08:43
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangGudang;
import co.id.bcafinance.hlbooking.model.projectakhir.pengajuan.cabang.DetailPengajuanGudangCabang;
import co.id.bcafinance.hlbooking.model.projectakhir.pengajuan.cabang.PengajuanGudangCabang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DetailPengajuanGudangCabangRepo extends JpaRepository<DetailPengajuanGudangCabang, Long> {
    List<DetailPengajuanGudangCabang> findByPengajuanGudangCabangAndIsApprovedAndIsActive(PengajuanGudangCabang pengajuan, boolean app, boolean act);

    List<DetailPengajuanGudangCabang> findByPengajuanGudangCabangAndIsApprovedAndIsDiterimaAndIsActive(PengajuanGudangCabang pengajuan, boolean app, boolean dt, boolean act);

}
