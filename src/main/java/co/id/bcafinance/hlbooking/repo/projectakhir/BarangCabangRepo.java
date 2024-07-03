package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/05/2024 14:36
@Last Modified 28/05/2024 14:36
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.BarangCabang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BarangCabangRepo extends JpaRepository<BarangCabang, Long> {
    boolean existsByBarangAndCabangAndIsActive(Barang idBarang, String cb, Boolean ac);

    @Query("SELECT bc FROM BarangCabang bc " +
            "JOIN bc.barang b " +
            "WHERE bc.cabang = :cabang " +
            " AND (:kat IS NULL OR b.kategoriBarang = :kat)" +
            "AND bc.isActive = :isActive " +
            "AND (LOWER(b.namaBarang) LIKE LOWER(CONCAT('%', :searchTerm, '%')))"+
            "AND (LOWER(b.kodeBarang) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<BarangCabang> findByCabangAndKategoriBarangAndSearch(@Param("cabang") String cabang,
                                                          @Param("kat") KategoriBarang kat,
                                                          @Param("isActive") Boolean isActive,
                                                          @Param("searchTerm") String searchTerm,
                                                          Pageable pageable);


    Optional<BarangCabang> findByBarangAndCabangAndIsActive(Barang id, String cb, Boolean ac);

    @Query("SELECT bc FROM BarangCabang bc" +
            " JOIN bc.barang b" +
            " WHERE bc.cabang = :cabang " +
            " AND bc.isActive = :isActive" +
            " AND b.kategoriBarang = :kategori")
    List<BarangCabang> listByCabangAndKategori(@Param("isActive") boolean isActive,
                                         @Param("kategori") KategoriBarang kategori,
                                         @Param("cabang") String cabang);


}
