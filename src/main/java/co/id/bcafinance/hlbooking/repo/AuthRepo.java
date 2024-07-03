package co.id.bcafinance.hlbooking.repo;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:44
@Last Modified 05/05/2024 21:44
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.model.projectakhir.Unit;
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
public interface AuthRepo extends JpaRepository<User,Long> {

    Optional<User> findTop1ByUsernameAndIsActive(String usr, Boolean act);

    Optional<User> findTop1ByIdUser(Long id);

    Optional<User> findTop1ByUsernameAndIsApproved(String usr, Boolean apprv);


    @Query("SELECT u FROM User u" +
            " WHERE u.isActive = :isActive AND u.isApproved = :isApproved" +
            " AND (LOWER(u.nama) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> findAllNotApprovedUserBySearchTerm(@Param("isActive") boolean isActive,
                                                  @Param("isApproved") boolean isApproved,
                                                  @Param("searchTerm") String searchTerm,
                                                  Pageable pageable);

    /**
     * Query untuk mencari existing user yang sudah terverifikasi dengan token berdasarkan username, nomor telpon, atau email
     */
//    Optional<User> findTop1ByUsernameOrNoTeleponOrEmailAndIsRegistered(String usr, String noHp, String mail, Boolean isRegis);

}

