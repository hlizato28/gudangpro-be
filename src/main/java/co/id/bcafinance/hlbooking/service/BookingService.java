//package co.id.bcafinance.hlbooking.service;
///*
//IntelliJ IDEA 2022.3.1 (Community Edition)
//Build #IC-223.8214.52, built on December 20, 2022
//@Author Heinrich a.k.a. Heinrich Lizato
//Java Developer
//Created on 05/05/2024 21:48
//@Last Modified 05/05/2024 21:48
//Version 1.0
//*/
//
//import co.id.bcafinance.hlbooking.configuration.OtherConfig;
//import co.id.bcafinance.hlbooking.core.security.ModulAuthority;
//import co.id.bcafinance.hlbooking.dto.BookingDTO;
//import co.id.bcafinance.hlbooking.handler.RequestCapture;
//import co.id.bcafinance.hlbooking.handler.ResponseHandler;
//import co.id.bcafinance.hlbooking.model.Booking;
//import co.id.bcafinance.hlbooking.model.Lapangan;
//import co.id.bcafinance.hlbooking.model.User;
//import co.id.bcafinance.hlbooking.repo.BookingRepo;
//import co.id.bcafinance.hlbooking.repo.LapanganRepo;
//import co.id.bcafinance.hlbooking.repo.UserRepo;
//import co.id.bcafinance.hlbooking.util.GlobalFunction;
//import co.id.bcafinance.hlbooking.util.LogTable;
//import co.id.bcafinance.hlbooking.util.LoggingFile;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.servlet.http.HttpServletRequest;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class BookingService {
//    @Autowired
//    BookingRepo bookingRepo;
//
//    @Autowired
//    UserRepo userRepo;
//
//    @Autowired
//    private ModulAuthority modulAuthority;
//
//    @Autowired
//    LapanganRepo lapanganRepo;
//
//    @Autowired
//    private ModelMapper modelMapper;
//    @Autowired
//    private GlobalFunction globalFunction;
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private String[] strExceptionArr = new String[2];
//    private Map<String, Object> mapToken = new HashMap<>();
//
//    /**
//     * Method untuk mencari lapangan yang available untuk di booking berdasarkan tanggal, jam mulai booking dan jam selesai booking
//     *
//     * @param bookingDTO    Data pencarian, yaitu tanggal, jam mulai booking dan jam selesai booking.
//     * @param pageable      Paging.
//     * @return              Data lapangan yang available.
//     */
//    public Page<BookingDTO> findAvailableLapangan(BookingDTO bookingDTO, Pageable pageable) {
//        LocalDateTime requestedDateTime = LocalDateTime.of(bookingDTO.getTanggal(), bookingDTO.getJamMulaiBooking());
//        LocalDateTime now = LocalDateTime.now();
//
//        if (requestedDateTime.isBefore(now)) {
//            return Page.empty();
//        }
//
//        Page<Lapangan> lapanganPage = lapanganRepo.findAvailableLapangan(
//                bookingDTO.getTanggal(),
//                bookingDTO.getJamMulaiBooking(),
//                bookingDTO.getJamSelesaiBooking(),
//                pageable);
//
//        return lapanganPage.map(lapangan -> {
//            BookingDTO dto = new BookingDTO();
//            dto.setNamaLapangan(lapangan.getNamaLapangan());
//            dto.setTanggal(bookingDTO.getTanggal());
//            dto.setJamMulaiBooking(bookingDTO.getJamMulaiBooking());
//            dto.setJamSelesaiBooking(bookingDTO.getJamSelesaiBooking());
//            return dto;
//        });
//    }
//
//    /**
//     * Method untuk membuat booking baru
//     *
//     * @param bookingDTO        Data booking, yaitu nama lapangan, tanggal, jam mulai booking dan jam selesai booking.
//     * @param request           Permintaan HTTP.
//     * @param authentication    Autentikasi jwt user
//     * @return                  ResponseEntity yang berisi hasil dari operasi.
//     */
//    public ResponseEntity<Object> createBooking(BookingDTO bookingDTO, HttpServletRequest request, Authentication authentication) {
//        Optional<Lapangan> optionalLapangan = lapanganRepo.findByNamaLapangan(bookingDTO.getNamaLapangan());
//        if (optionalLapangan.isEmpty()) {
//            return globalFunction.dataNotFoundById(request);
//        }
//
//        Optional<User> optionalUser = userRepo.findByUsername((authentication.getName()));
//        if (optionalUser.isEmpty()) {
//            return globalFunction.dataNotFoundById(request);
//        }
//
//        Lapangan l = optionalLapangan.get();
//        User u = optionalUser.get();
//        boolean hasAdminRole = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
//
//        if (!hasAdminRole && !u.getUsername().equals(authentication.getName())) {
//            return new ResponseHandler().generateResponse("Anda tidak diizinkan untuk membuat booking pengguna lain!!",
//                    HttpStatus.FORBIDDEN,
//                    null,
//                    "FE04031", request);
//        }
//
//        try {
//            Booking booking = new Booking();
//            booking.setLapangan(l);
//            booking.setTanggalBooking(bookingDTO.getTanggal());
//            booking.setJamMulaiBooking(bookingDTO.getJamMulaiBooking());
//            booking.setJamSelesaiBooking(bookingDTO.getJamSelesaiBooking());
//            booking.setOrderTime(LocalTime.now());
//            booking.setPayment(false);
//            booking.setUser(u);
//
//            bookingRepo.save(booking);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseHandler().generateResponse("Booking Gagal Dibuat!!",
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    null,
//                    "FE04031", request);
//        }
//
//        return new ResponseHandler().generateResponse("Booking Berhasil Dibuat!!",
//                HttpStatus.OK,
//                null,
//                null, request);
//    }
//
//    /**
//     * Method untuk mencari booking yang masih valid (belum lewat waktunya) berdasarkan id user
//     *
//     * @param userId        Id dari user.
//     * @param pageable      Paging.
//     * @return              Booking yang masih valid.
//     */
//    public Page<BookingDTO> findValidBookingByUserId(Long userId, Pageable pageable) {
//        LocalDate dateNow = LocalDate.now();
//        LocalTime hourNow = LocalTime.now();
//
//        return bookingRepo.findValidBookingByUserId(userId, dateNow, hourNow, pageable)
//                .map(booking -> modelMapper.map(booking, BookingDTO.class));
//    }
//
//    /**
//     * Method untuk menghapus booking yang masih valid (belum lewat waktunya) berdasarkan id user dan id booking
//     *
//     * @param user              Id dari user.
//     * @param book              Id dari Booking.
//     * @param request           Permintaan HTTP
//     * @param authentication    Autentikasi jwt user
//     * @return                  ResponseEntity yang berisi hasil dari operasi.
//     */
//    public ResponseEntity<Object> doDelete(Long user, Long book, HttpServletRequest request, Authentication authentication) {
//        mapToken = modulAuthority.checkAuthorization(request);
//
//        Optional<User> optionalUser = userRepo.findById(user);
//        if (optionalUser.isEmpty()) {
//            return globalFunction.dataNotFoundById(request);
//        }
//
//        User u = optionalUser.get();
//        boolean hasAdminRole = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
//
//        if (!hasAdminRole && !u.getUsername().equals(authentication.getName())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Anda tidak diizinkan untuk menghapus data pengguna lain.");
//        }
//
//        try {
//            bookingRepo.deleteById(book);
//        } catch (Exception e) {
//
//            new LogTable().inputLogRequest(strExceptionArr, e, OtherConfig.getFlagLogTable(), Long.parseLong(mapToken.get("de").toString()), OtherConfig.getUrlAPILogRequestError(), request);
//            return new ResponseHandler().generateResponse("Data Gagal Dihapus",
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    null,
//                    "FE04031", request);//FAILED VALIDATION
//        }
//        return new ResponseHandler().generateResponse("Data Berhasil Dihapus!!",
//                HttpStatus.OK,
//                null,
//                null, request);
//    }
//
//    /**
//     * Method untuk update booking yang masih valid (belum lewat waktunya) berdasarkan id booking
//     *
//     * @param id                Id dari Booking.
//     * @param bookingDTO        Data booking, yaitu nama lapangan, tanggal, jam mulai booking dan jam selesai booking.
//     * @param request           Permintaan HTTP
//     * @return                  ResponseEntity yang berisi hasil dari operasi.
//     */
//    public ResponseEntity<Object> doEdit(Long id, BookingDTO bookingDTO, HttpServletRequest request) {
//        Optional<Booking> optionalBooking = bookingRepo.findById(id);
//        if (optionalBooking.isEmpty()) {
//            return globalFunction.dataNotFoundById(request);
//        }
//
//        Optional<Lapangan> optionalLapangan = lapanganRepo.findByNamaLapangan(bookingDTO.getNamaLapangan());
//        if (optionalLapangan.isEmpty()) {
//            return globalFunction.dataNotFoundById(request);
//        }
//
//        Booking b = optionalBooking.get();
//        Lapangan l = optionalLapangan.get();
//
//        try {
//            b.setLapangan(l);
//            b.setTanggalBooking(bookingDTO.getTanggal());
//            b.setJamMulaiBooking(bookingDTO.getJamMulaiBooking());
//            b.setJamSelesaiBooking(bookingDTO.getJamSelesaiBooking());
//            l.setUpdatedBy("admin");
//            l.setUpdatedAt(new Date());
//        } catch (Exception e) {
//            strExceptionArr[1] = "edit(Long id, Booking booking, HttpServletRequest request)" + RequestCapture.allRequest(request);
//            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLoging());
//
//            return new ResponseHandler().generateResponse("Data Booking Gagal Diubah !! ",
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    null,
//                    "FE04021", request);
//        }
//        return new ResponseHandler().generateResponse("Data Booking Berhasil Diubah!!",
//                HttpStatus.OK,
//                null,
//                null, request);
//    }
//
//    /**
//     * Method untuk mencari semua booking yang masih valid (belum lewat waktunya)
//     *
//     * @param pageable          Paging.
//     * @param searchTerm        Filter untuk melakukan pencarian booking tertentu.
//     * @return                  Data booking yang masih valid.
//     */
//    public Page<BookingDTO> getAllValidBooking(Pageable pageable, String searchTerm) {
//        LocalDate dateNow = LocalDate.now();
//        LocalTime hourNow = LocalTime.now();
//
//        Page<Booking> bookingPage = bookingRepo.findAllValidBooking(dateNow, hourNow, searchTerm.toLowerCase(), pageable);
//
//        return bookingPage.map(booking -> {
//            BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
//
//            Lapangan lapangan = booking.getLapangan();
//            if (lapangan != null) {
//                bookingDTO.setNamaLapangan(lapangan.getNamaLapangan());
//            }
//
//            User user = booking.getUser();
//            if (user != null) {
//                bookingDTO.setMember(user.getUsername());
////                bookingDTO.setNoTelepon(user.getNoTelepon());
//            }
//
//            return bookingDTO;
//        });
//    }
//
//}
//
