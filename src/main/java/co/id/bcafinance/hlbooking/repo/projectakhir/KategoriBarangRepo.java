package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 27/05/2024 10:48
@Last Modified 27/05/2024 10:48
Version 1.0
*/


import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KategoriBarangRepo extends JpaRepository<KategoriBarang,Long> {
    Optional<KategoriBarang> findByNamaKategori(String nama);

    List<KategoriBarang> findAllByIsActiveOrderByNamaKategoriAsc(Boolean act);
    boolean existsByNamaKategoriAndIsActive(String namaKategori, Boolean active);
}
