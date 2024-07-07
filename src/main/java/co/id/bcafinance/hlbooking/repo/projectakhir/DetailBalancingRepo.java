package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/06/2024 11:30
@Last Modified 28/06/2024 11:30
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.barang.Balancing;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.DetailBalancing;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangGudang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DetailBalancingRepo extends JpaRepository<DetailBalancing, Long> {
    Optional<DetailBalancing> findByBarangGudangAndBalancingAndIsActive(BarangGudang barangGudang, Long id, boolean act);

    @Query("SELECT db FROM DetailBalancing db " +
            "WHERE db.barangGudang = :barangGudang AND db.isActive = :isActive " +
            "ORDER BY CASE WHEN db.updatedAt IS NULL THEN db.createdAt ELSE db.updatedAt END DESC")
    Page<DetailBalancing> findLatestByBarangGudangAndIsActive(@Param("barangGudang") BarangGudang barangGudang,
                                                              @Param("isActive") boolean isActive,
                                                              Pageable pageable);

    default Optional<DetailBalancing> findTopLatestByBarangGudangAndIsActive(BarangGudang barangGudang, boolean isActive) {
        Page<DetailBalancing> page = findLatestByBarangGudangAndIsActive(barangGudang, isActive, PageRequest.of(0, 1));
        return page.getContent().stream().findFirst();
    }

    @Query("SELECT bg, db FROM BarangGudang bg " +
            "JOIN bg.barangCabang bc " +
            "JOIN bc.barang b " +
            "LEFT JOIN DetailBalancing db ON bg.idBarangGudang = db.barangGudang.idBarangGudang " +
            "AND db.balancing IS NULL AND db.isActive = true " +
            "AND (CONVERT(DATE, COALESCE(db.updatedAt, db.createdAt)) = :tanggal) " +
            "WHERE bc.cabang = :cabang AND b.kategoriBarang = :kat AND bg.isActive = true " +
            "AND (db.idDetailBalancing IS NOT NULL OR (bg.jumlah > 0 AND bg.idBarangGudang NOT IN " +
            "(SELECT DISTINCT db2.barangGudang.idBarangGudang FROM DetailBalancing db2 " +
            "WHERE db2.balancing IS NULL AND db2.isActive = true " +
            "AND CONVERT(DATE, COALESCE(db2.updatedAt, db2.createdAt)) = :tanggal)))")
    Page<Object[]> findBarangGudangAndDetailBalancing(@Param("cabang") String cabang,
                                                      @Param("kat") KategoriBarang kat,
                                                      @Param("tanggal") Date tanggal,
                                                      Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(db) > 0 THEN true ELSE false END " +
            "FROM DetailBalancing db " +
            "JOIN db.barangGudang bg " +
            "JOIN bg.barangCabang bc " +
            "JOIN bc.barang b " +
            "WHERE bc.cabang = :cabang AND b.kategoriBarang = :kat " +
            "AND db.balancing IS NOT NULL AND db.isActive = true " +
            "AND CONVERT(DATE, db.createdAt) = :tanggal")
    boolean existByKategoriBarangAndTanggal(@Param("cabang") String cabang,
                                            @Param("kat") KategoriBarang kat,
                                            @Param("tanggal") Date tanggal);

    @Query("SELECT db " +
            "FROM DetailBalancing db " +
            "JOIN db.balancing bl " +
            "JOIN db.barangGudang bg " +
            "JOIN bg.barangCabang bc " +
            "JOIN bc.barang b " +
            "WHERE bl.isApproved = :app AND bl.isRevisi = false " +
            "AND CONVERT(DATE, bl.createdAt) = :tanggal " +
            "AND bc.cabang = :cabang " +
            "AND b.kategoriBarang = :kategori")
    Page<DetailBalancing> findFilteredDetailBalancing(@Param("cabang") String cabang,
                                                      @Param("app") Boolean app,
                                                      @Param("kategori") KategoriBarang kategori,
                                                      @Param("tanggal") Date tanggal,
                                                      Pageable pageable);

    @Query("SELECT dp.barangGudang.idBarangGudang, ug.namaUnitGroup, SUM(dp.jumlahDiterima) " +
            "FROM DetailPengajuanGudangCabang dp " +
            "JOIN dp.pengajuanGudangCabang p " +
            "JOIN p.user u " +
            "JOIN u.unit un " +
            "JOIN un.unitGroup ug " +
            "WHERE dp.barangGudang.idBarangGudang IN :idBarangGudangList " +
            "AND CONVERT(DATE, dp.updatedAt) = :tanggal " +
            "AND dp.isDiterima = true " +
            "GROUP BY dp.barangGudang.idBarangGudang, ug.namaUnitGroup")
    List<Object[]> findPengajuanDataByBarangGudangIds(@Param("idBarangGudangList") List<Long> idBarangGudangList,
                                                      @Param("tanggal") Date tanggal);

    Optional<DetailBalancing> findTop1ByBalancing(Balancing balancing);

    Page<DetailBalancing> findByBalancingAndIsActive(Balancing balancing, Boolean act, Pageable pageable);


}




