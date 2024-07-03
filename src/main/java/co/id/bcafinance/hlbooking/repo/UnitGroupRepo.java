package co.id.bcafinance.hlbooking.repo;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 13/06/2024 17:06
@Last Modified 13/06/2024 17:06
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.UnitGroup;
import co.id.bcafinance.hlbooking.model.projectakhir.Unit;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UnitGroupRepo extends JpaRepository<UnitGroup, Long> {
    Optional<UnitGroup> findByNamaUnitGroupAndIsActive(String nama, Boolean actv);

    boolean existsByNamaUnitGroupAndIsActive(String nama, Boolean active);

    List<UnitGroup> findAllByIsActiveOrderByNamaUnitGroupAsc(Boolean isActive);

    @Query("SELECT ug FROM UnitGroup ug" +
            " WHERE ug.isActive = :isActive" +
            " AND (LOWER(ug.namaUnitGroup) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<UnitGroup> findAllUnitGroupBySearchTerm(@Param("isActive") boolean isActive,
                                                 @Param("searchTerm") String searchTerm,
                                                 Pageable pageable);
}
