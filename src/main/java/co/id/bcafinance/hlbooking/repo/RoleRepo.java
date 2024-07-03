package co.id.bcafinance.hlbooking.repo;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 06/05/2024 09:19
@Last Modified 06/05/2024 09:19
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.Role;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@EnableTransactionManagement
public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByNamaRoleAndIsActive(String nama, Boolean ac);

    boolean existsByNamaRoleAndIsActive(String role, Boolean active);

    List<Role> findAllByIsActiveOrderByNamaRoleAsc(Boolean act);

    Optional<Role> findByIdRoleAndIsActive(Long id, Boolean ac);

    @Query("SELECT r FROM Role r" +
            " WHERE r.isActive = :isActive" +
            " AND (LOWER(r.namaRole) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Role> findByIsActiveContainingIgnoreCase(@Param("isActive") boolean isActive,
                                                                             @Param("searchTerm") String searchTerm,
                                                                             Pageable pageable);

}
