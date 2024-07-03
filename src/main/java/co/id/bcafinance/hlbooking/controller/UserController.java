package co.id.bcafinance.hlbooking.controller;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 20:40
@Last Modified 05/05/2024 20:40
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.UserDTO;
import co.id.bcafinance.hlbooking.dto.projectakhir.barang.BarangDTO;
import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/u")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * @desc    Update data user berdasarkan id user
     * @route   POST /api/u/edit/:idUser
     * @access  Public
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> editUser(@Valid @RequestBody UserDTO userDTO,
                                           @PathVariable(value = "id") Long id,
                                           HttpServletRequest request){

        User user = modelMapper.map(userDTO, new TypeToken<User>() {}.getType());
        return userService.edit(id,userDTO,request);
    }

    /**
     * @desc    Delete data user berdasarkan id user
     * @route   POST /api/u/delete/:idUser
     * @access  Public
     */
    @PutMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long id,
                                             HttpServletRequest request)
    {
        return userService.delete(id,request);
    }

    /**
     * @desc    Mencari data user berdasarkan id user
     * @route   POST /api/u/:idUser
     * @access  Public
     */
    @GetMapping("/data/{id}")
    public ResponseEntity<Object> findByIdUser( @PathVariable(value = "id") Long id,
                                                HttpServletRequest request){
        return userService.findById(id,request);
    }

    /**
     * @desc    Mencari data user yang memiliki role member
     * @route   POST /api/u/member
     * @access  Private (Admin)
     */
//    @GetMapping("/member")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<Page<UserDTO>> getAllMember(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "6") int size,
//            @RequestParam(defaultValue = "") String searchTerm) {
//        Page<UserDTO> userDTOPage = userService.findUserByRole(PageRequest.of(page, size), searchTerm);
//        return ResponseEntity.ok(userDTOPage);
//    }

    @GetMapping("/user/approved")
    public ResponseEntity<Page<UserDTO>> getAllApprovedUser(
            @RequestParam(defaultValue = "") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userDTOPage = userService.findAll(true, role, pageable, searchTerm, request);
        return ResponseEntity.ok(userDTOPage);
    }

    @GetMapping("/user/not-approved")
    public ResponseEntity<Page<UserDTO>> getAllNotApprovedUser(
            @RequestParam(defaultValue = "") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userDTOPage = userService.findAll(false, role, pageable, searchTerm, request);
        return ResponseEntity.ok(userDTOPage);
    }

    @GetMapping("/current-user")
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request){
        return userService.getCurrentUserInfo(request);
    }




}

