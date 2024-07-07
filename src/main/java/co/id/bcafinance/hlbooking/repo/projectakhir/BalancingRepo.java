package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 30/06/2024 14:57
@Last Modified 30/06/2024 14:57
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.barang.Balancing;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BalancingRepo extends JpaRepository<Balancing,Long> {

    @Query("SELECT DISTINCT b " +
            "FROM Balancing b " +
            "JOIN DetailBalancing db ON db.balancing.idBalancing = b.idBalancing " +
            "JOIN db.barangGudang bg " +
            "JOIN bg.barangCabang bc " +
            "JOIN bc.barang br " +
            "WHERE b.isRevisi = true " +
            "AND b.isActive = true " +
            "AND bc.cabang = :cabang " +
            "ORDER BY b.createdAt DESC")
    Page<Balancing> findByIsRevisiAndIsActiveAndCabang(@Param("cabang") String cabang,
                                                       Pageable pageable);
}
