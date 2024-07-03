//package co.id.bcafinance.hlbooking.repo;
///*
//IntelliJ IDEA 2022.3.1 (Community Edition)
//Build #IC-223.8214.52, built on December 20, 2022
//@Author Heinrich a.k.a. Heinrich Lizato
//Java Developer
//Created on 05/05/2024 21:45
//@Last Modified 05/05/2024 21:45
//Version 1.0
//*/
//
//import co.id.bcafinance.hlbooking.model.Lapangan;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Optional;
//
//@Transactional
//@EnableTransactionManagement
//public interface LapanganRepo extends JpaRepository<Lapangan,Long> {
//
//    /**
//     * Query untuk mencari semua data lapangan
//     */
//    Optional<Lapangan> findByNamaLapangan(String nama);
//
//    /**
//     * Query untuk mencari lapangan yang available untuk di booking berdasarkan tanggal, jam mulai booking, jam selesai booking dan existing booking
//     */
//    @Query("SELECT l FROM Lapangan l " +
//            "WHERE CAST(l.jamMulai AS time) <= CAST(:jamMulaiBooking AS time) " +
//            "AND CAST(l.jamSelesai AS time) >= CAST(:jamSelesaiBooking AS time) " +
//            "AND NOT EXISTS (" +
//            "SELECT 1 FROM Booking b WHERE b.lapangan.idLapangan = l.idLapangan " +
//            "AND b.tanggalBooking = :tanggal " +
//            "AND CAST(b.jamMulaiBooking AS time) < CAST(:jamSelesaiBooking AS time) " +
//            "AND CAST(b.jamSelesaiBooking AS time) > CAST(:jamMulaiBooking AS time))")
//    Page<Lapangan> findAvailableLapangan(@Param("tanggal") LocalDate tanggal,
//                                         @Param("jamMulaiBooking") LocalTime jamMulaiBooking,
//                                         @Param("jamSelesaiBooking") LocalTime jamSelesaiBooking,
//                                         Pageable pageable);
//
//    /**
//     * Query untuk mencari lapangan tertentu berdasarkan nama lapangan
//     */
//    Page<Lapangan> findByNamaLapanganContainingIgnoreCase(String searchTerm, Pageable pageable);
//
//
//}
