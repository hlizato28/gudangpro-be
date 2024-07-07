package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 27/06/2024 08:42
@Last Modified 27/06/2024 08:42
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangGudang;
import co.id.bcafinance.hlbooking.model.projectakhir.pengajuan.cabang.PengajuanGudangCabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PengajuanGudangCabangRepo extends JpaRepository <PengajuanGudangCabang, Long> {
    @Query("SELECT DISTINCT p FROM PengajuanGudangCabang p " +
            "JOIN p.user u " +
            "JOIN DetailPengajuanGudangCabang d ON d.pengajuanGudangCabang = p " +
            "WHERE u.cabang = :cabang " +
            "AND d.isApproved = :app " +
            "AND d.isActive = :active " +
            "AND p.isActive = :active")
    Page<PengajuanGudangCabang> findPengajuanDetailsByCabangAndNotApproved(@Param("cabang") String cabang,
                                                                           @Param("active") Boolean active,
                                                                           @Param("app") Boolean app,
                                                                           Pageable pageable);

    @Query("SELECT DISTINCT p FROM PengajuanGudangCabang p " +
            "JOIN p.user u " +
            "JOIN DetailPengajuanGudangCabang d ON d.pengajuanGudangCabang = p " +
            "WHERE d.isApproved = :app " +
            "AND p.user = :user " +
            "AND d.isActive = :active " +
            "AND p.isActive = :active " +
            "AND d.isDiterima = :dt")
    List<PengajuanGudangCabang> findPengajuanDetailsByUserAndApproved(@Param("user") User user,
                                                                      @Param("active") Boolean active,
                                                                      @Param("app") Boolean app,
                                                                      @Param("dt") Boolean dt);

    @Query("SELECT DISTINCT p FROM PengajuanGudangCabang p " +
            "JOIN DetailPengajuanGudangCabang d ON d.pengajuanGudangCabang = p " +
            "WHERE d.barangGudang = :bg " +
            "AND d.isApproved = true " +
            "AND d.isActive = true " +
            "AND CONVERT(date, d.updatedAt) = CONVERT(date, :tgl) " +
            "AND p.isActive = true")
    Page<PengajuanGudangCabang> findPengajuanDetailsByBarangGudangAndTanggal(@Param("bg") BarangGudang bg,
                                                                             @Param("tgl") Date tgl,
                                                                             Pageable pageable);
}
