package co.id.bcafinance.hlbooking.repo.projectakhir;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 28/05/2024 13:16
@Last Modified 28/05/2024 13:16
Version 1.0
*/

import co.id.bcafinance.hlbooking.model.projectakhir.Cabang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CabangRepo extends JpaRepository<Cabang, Long> {
    boolean existsByNamaCabangAndIsActive(String nama, Boolean active);
}
