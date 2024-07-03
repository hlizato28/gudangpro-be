//package co.id.bcafinance.hlbooking.controller;
///*
//IntelliJ IDEA 2022.3.1 (Community Edition)
//Build #IC-223.8214.52, built on December 20, 2022
//@Author Heinrich a.k.a. Heinrich Lizato
//Java Developer
//Created on 05/05/2024 20:39
//@Last Modified 05/05/2024 20:39
//Version 1.0
//*/
//
//import co.id.bcafinance.hlbooking.dto.BookingDTO;
//import co.id.bcafinance.hlbooking.model.Booking;
//import co.id.bcafinance.hlbooking.service.BookingService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping("/api/b")
//public class BookingController {
//    @Autowired
//    BookingService bookingService;
//
//    @Autowired
//    ModelMapper modelMapper;
//
//    /**
//     * @desc    Mencari lapangan yang available untuk di booking berdasarkan tanggal, jam mulai booking dan jam selesai booking
//     * @route   POST /api/b/find/lapangan
//     * @access  Public
//     */
//    @PostMapping("/find/lapangan")
//    public ResponseEntity<Page<BookingDTO>> findAvailableLapangan(
//            @RequestBody BookingDTO bookingRequest,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "6") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<BookingDTO> availableLapangans = bookingService.findAvailableLapangan(bookingRequest, pageable);
//        return ResponseEntity.ok(availableLapangans);
//    }
//
//    /**
//     * @desc    Membuat booking baru
//     * @route   POST /api/b/create
//     * @access  Public
//     */
//    @PostMapping("/create")
//    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingDTO bookingDTO,
//                                                HttpServletRequest request,
//                                                Authentication authentication){
//        return bookingService.createBooking(bookingDTO, request, authentication);
//    }
//
//    /**
//     * @desc    Mencari booking yang masih valid (belum lewat waktunya) berdasarkan id user
//     * @route   POST /api/b/valid/:idUser
//     * @access  Public
//     */
//    @GetMapping("/valid/{id}")
//    public ResponseEntity<Page<BookingDTO>> getValidBookingByUserId(
//            @PathVariable(value = "id") Long id,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "6") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<BookingDTO> validBookings = bookingService.findValidBookingByUserId(id, pageable);
//        return ResponseEntity.ok(validBookings);
//    }
//
//    /**
//     * @desc    Menghapus booking yang masih valid (belum lewat waktunya) berdasarkan id user dan id booking
//     * @route   POST /api/b/delete/:idUser/:idBooking
//     * @access  Public
//     */
//    @DeleteMapping("/delete/{user}/{book}")
//    public ResponseEntity<Object> deleteUser(@PathVariable(value = "user") Long user,
//                                             @PathVariable(value = "book") Long book,
//                                             HttpServletRequest request,
//                                             Authentication authentication){
//        return bookingService.doDelete(user, book, request, authentication);
//    }
//
//    /**
//     * @desc    Update booking yang masih valid (belum lewat waktunya) berdasarkan id booking
//     * @route   POST /api/b/edit/:idBooking
//     * @access  Private (Admin)
//     */
//    @PutMapping("/edit/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<Object> editBooking(@Valid @RequestBody BookingDTO bookingDTO,
//                                              @PathVariable(value = "id") Long id,
//                                              HttpServletRequest request){
//
//        Booking booking = modelMapper.map(bookingDTO, Booking.class);
//        return bookingService.doEdit(id,bookingDTO,request);
//    }
//
//    /**
//     * @desc    Mencari semua booking yang masih valid (belum lewat waktunya)
//     * @route   POST /api/b/all
//     * @access  Private (Admin)
//     */
//    @GetMapping("/all")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<Page<BookingDTO>> getAllBooking(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "6") int size,
//            @RequestParam(defaultValue = "") String searchTerm) {
//        Page<BookingDTO> bookingDTOPage = bookingService.getAllValidBooking(PageRequest.of(page, size), searchTerm);
//        return ResponseEntity.ok(bookingDTOPage);
//    }
//
//}
//
