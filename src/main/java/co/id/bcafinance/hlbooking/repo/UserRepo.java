package co.id.bcafinance.hlbooking.repo;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:45
@Last Modified 05/05/2024 21:45
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.Role;
import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.Barang;
import co.id.bcafinance.hlbooking.model.projectakhir.barang.KategoriBarang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@EnableTransactionManagement
public interface UserRepo extends JpaRepository<User,Long> {

    /**
     * Query untuk mencari data user berdasarkan username
     */
    Optional<User> findByUsername(String nama);

    /**
     * Query untuk mencari role dari data user tertentu
     */
    Page<User> findAllByRole_IdRole(Long idRole, Pageable pageable);

    /**
     * Query untuk mencari user tertentu berdasarkan username, email, nomor telepon atau nama
     */
//    @Query("SELECT u FROM User u " +
//            "WHERE u.role.id = :roleId AND (" +
//            "LOWER(u.username) LIKE %:searchTerm% OR " +
//            "LOWER(u.email) LIKE %:searchTerm% OR " +
//            "LOWER(u.nama) LIKE %:searchTerm% OR " +
//            "u.noTelepon LIKE %:searchTerm%)")
//    Page<User> findByRoleIdAndSearchTerm( @Param("roleId") Long roleId,
//                                          @Param("searchTerm") String searchTerm,
//                                          Pageable pageable);

//    @Query("SELECT u FROM User u JOIN u.unit unit" +
//            " WHERE u.role = :role AND u.isActive = :isActive" +
//            " AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
//            " OR LOWER(u.nama) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
//            " OR LOWER(u.cabang) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
//            " OR LOWER(u.jabatan) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
//            " OR LOWER(unit.namaUnit) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
//    Page<User> findByRoleIdAndSearchTerm( @Param("role") Role role,
//                                          @Param("isActive") boolean isActive,
//                                          @Param("searchTerm") String searchTerm,
//                                          Pageable pageable);



    @Query("SELECT u FROM User u JOIN u.unit unit" +
            " WHERE u.role = :role AND u.isActive = :isActive" +
            " AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(u.nama) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(u.cabang) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(u.jabatan) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(unit.namaUnit) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> findByIDRoleAndSearchTermAndIsActive(@Param("role") Role role,
                                                      @Param("isActive") boolean isActive,
                                                      @Param("searchTerm") String searchTerm,
                                                      Pageable pageable);

    boolean existsByUsernameAndIsActive(String un, Boolean ac);

    Optional<User> findByIdUserAndIsActive(Long id, Boolean ac);

    @Query("SELECT u FROM User u" +
            " WHERE u.isActive = :isActive AND u.isApproved = :app" +
            " AND (:role IS NULL OR u.role = :role)" +
            " AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(u.nama) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(u.cabang) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(u.jabatan) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(unit.namaUnit) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> findAll(@Param("role") Role role,
                       @Param("isActive") boolean isActive,
                       @Param("app") boolean app,
                       @Param("searchTerm") String searchTerm,
                       Pageable pageable);





}
