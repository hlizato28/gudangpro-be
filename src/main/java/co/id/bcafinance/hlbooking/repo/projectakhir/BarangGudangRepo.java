package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 23/06/2024 23:44
@Last Modified 23/06/2024 23:44
Version 1.0
*/


import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangCabang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangGudang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BarangGudangRepo extends JpaRepository<BarangGudang, Long> {
    boolean existsByBarangCabangAndIsActive(BarangCabang id, Boolean ac);

    @Query("SELECT bg FROM BarangGudang bg" +
            " JOIN bg.barangCabang bc" +
            " JOIN bc.barang b" +
            " WHERE bg.isActive = :isActive" +
            " AND bc.cabang = :cabang" +
            " AND bg.createApproved = :ca" +
            " AND bg.deleteToBeApprove = :da" +
            " AND (:kategori IS NULL OR b.kategoriBarang = :kategori)" +
            " AND (LOWER(b.kodeBarang) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(b.namaBarang) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<BarangGudang> findAll(@Param("isActive") boolean isActive,
                               @Param("cabang") String cabang,
                               @Param("ca") boolean ca,
                               @Param("da") boolean da,
                               @Param("searchTerm") String searchTerm,
                               @Param("kategori") KategoriBarang kategori,
                               Pageable pageable);

    @Query("SELECT bg FROM BarangGudang bg" +
            " JOIN bg.barangCabang bc" +
            " JOIN bc.barang b" +
            " WHERE bg.isActive = :isActive" +
            " AND b.namaBarang = :nama " +
            " AND bc.cabang = :cabang" +
            " AND bg.createApproved = :ca" +
            " AND bg.deleteToBeApprove = :da")
    Optional<BarangGudang> findByNamaBarang(@Param("isActive") boolean isActive,
                                            @Param("nama") String nama,
                                            @Param("cabang") String cabang,
                                            @Param("ca") boolean ca,
                                            @Param("da") boolean da);

    @Query("SELECT bg FROM BarangGudang bg" +
            " JOIN bg.barangCabang bc" +
            " JOIN bc.barang b" +
            " WHERE bg.isActive = :isActive" +
            " AND bc.cabang = :cabang" +
            " AND bg.createApproved = :ca" +
            " AND bg.deleteToBeApprove = :da" +
            " AND b.kategoriBarang = :kategori" +
            " AND bg.jumlah > 0")
    List<BarangGudang> listByCabangAndKategoriAndJumlah(@Param("isActive") boolean isActive,
                                                        @Param("cabang") String cabang,
                                                        @Param("ca") boolean ca,
                                                        @Param("da") boolean da,
                                                        @Param("kategori") KategoriBarang kategori);

    Optional<BarangGudang> findByBarangCabang_Barang_KodeBarangAndBarangCabang_Cabang(String kodeBarang, String cabang);
}





