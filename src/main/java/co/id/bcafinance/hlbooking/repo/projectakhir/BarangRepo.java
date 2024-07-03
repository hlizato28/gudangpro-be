package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 27/05/2024 11:19
@Last Modified 27/05/2024 11:19
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BarangRepo extends JpaRepository<Barang,Long> {
    boolean existsByNamaBarang(String namaBarang);
    boolean existsByKodeBarang(String kode);

    Optional<Barang> findByNamaBarang(String nama);

    @Query("SELECT b FROM Barang b" +
            " WHERE b.isActive = :isActive" +
            " AND (:kategori IS NULL OR b.kategoriBarang = :kategori)" +
            " AND (LOWER(b.kodeBarang) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(b.namaBarang) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Barang> findByNamaBarangOrKodeBarangAndIsActiveContainingIgnoreCase(@Param("kategori") KategoriBarang kategori,
                                                                             @Param("isActive") boolean isActive,
                                                                             @Param("searchTerm") String searchTerm,
                                                                             Pageable pageable);

    List<Barang> findAllByKategoriBarangAndIsActiveOrderByNamaBarangAsc(KategoriBarang k ,Boolean act);




}
