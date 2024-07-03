package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 13/06/2024 10:14
@Last Modified 13/06/2024 10:14
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.UnitGroup;
import co.id.bcafinance.hlbooking.model.projectakhir.Cabang;
import co.id.bcafinance.hlbooking.model.projectakhir.Unit;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UnitRepo extends JpaRepository<Unit, Long> {
    Optional<Unit> findByNamaUnitAndIsActive(String nama, Boolean active);



    boolean existsByNamaUnitAndIsActive(String unit, boolean active);



//    @Query("SELECT u FROM Unit u JOIN u.unitGroup ugp" +
//            " WHERE u.unitGroup = :ug AND u.isActive = :isActive" +
//            " AND (LOWER(u.namaUnit) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
//            " OR LOWER(ugp.namaUnitGroup) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
//    Page<Unit> findByUnitGroupAndIsActiveAndUnitOrUnitGroupContainingIgnoreCase(@Param("ug") UnitGroup ug,
//                                                                                  @Param("isActive") boolean isActive,
//                                                                                  @Param("searchTerm") String searchTerm,
//                                                                                  Pageable pageable);

    @Query("SELECT u FROM Unit u JOIN u.unitGroup ug" +
            " WHERE u.isActive = :isActive" +
            " AND (:ugroup IS NULL OR u.unitGroup = :ugroup)" +
            " AND (LOWER(u.namaUnit) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(ug.namaUnitGroup) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Unit> findAllWithGroup(@Param("isActive") boolean isActive,
                                @Param("ugroup") UnitGroup ugroup,
                                @Param("searchTerm") String searchTerm,
                                Pageable pageable);

    @Query("SELECT u FROM Unit u LEFT JOIN u.unitGroup ug" +
            " WHERE u.isActive = :isActive AND u.unitGroup IS NULL" +
            " AND (LOWER(u.namaUnit) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(ug.namaUnitGroup) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Unit> findAllWithoutGroup(@Param("isActive") boolean isActive,
                                   @Param("searchTerm") String searchTerm,
                                   Pageable pageable);

}
