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
//import co.id.bcafinance.hlbooking.model.Booking;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Transactional
//@EnableTransactionManagement
//public interface BookingRepo extends JpaRepository<Booking, Long> {
//
//    /**
//     * Query untuk mencari booking yang masih valid (belum lewat waktunya) milik user tertentu
//     */
//    @Query("SELECT b FROM Booking b " +
//            "WHERE b.user.id = :userId " +
//            "AND (b.tanggalBooking > :tanggal " +
//            "OR (b.tanggalBooking = :tanggal AND CAST(b.jamMulaiBooking AS time) > CAST(:jam AS time)))")
//    Page<Booking> findValidBookingByUserId(Long userId, LocalDate tanggal, LocalTime jam, Pageable pageable);
//
//    /**
//     * Query untuk mencari semua booking yang masih valid (belum lewat waktunya)
//     */
//    @Query("SELECT b FROM Booking b " +
//            "JOIN b.user u " +
//            "WHERE (b.tanggalBooking > :tanggal " +
//            "OR (b.tanggalBooking = :tanggal AND CAST(b.jamMulaiBooking AS time) > CAST(:jam AS time))) " +
//            "AND (LOWER(u.username) LIKE %:searchTerm% " +
//            "OR u.noTelepon LIKE %:searchTerm%)")
//    Page<Booking> findAllValidBooking(LocalDate tanggal, LocalTime jam, String searchTerm, Pageable pageable);
//
//
//
//}
