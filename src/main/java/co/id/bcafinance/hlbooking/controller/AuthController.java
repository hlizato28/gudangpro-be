package co.id.bcafinance.hlbooking.controller;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 20:39
@Last Modified 05/05/2024 20:39
Version 1.0
*/

import co.id.bcafinance.hlbooking.dto.UserDTO;
import co.id.bcafinance.hlbooking.dto.auth.LoginDTO;
import co.id.bcafinance.hlbooking.dto.auth.RegisDTO;
import co.id.bcafinance.hlbooking.dto.auth.VerifyDTO;
import co.id.bcafinance.hlbooking.model.User;
import co.id.bcafinance.hlbooking.service.AuthService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    ModelMapper modelMapper;

    /**
     * @desc    Registrasi akun user
     * @route   POST /api/auth/registration
     * @access  Public
     */
    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@Valid @RequestBody RegisDTO regisDTO,
                                               HttpServletRequest request) {
        return authService.regis(regisDTO,request);
    }

    /**
     * @desc    Login akun user
     * @route   POST /api/auth/login
     * @access  Public
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO,
                                        HttpServletRequest request) {
        return authService.login(loginDTO,request);
    }

    /**
     * @desc    Verify setelah regisrasi akun user
     * @route   POST /api/auth/registration/verify
     * @access  Public
     */
    @PutMapping("/registration/approve/{id}")
    public ResponseEntity<Object> approve(@PathVariable("id") Long id,
                                          @Valid @RequestBody UserDTO userDTO,
                                          HttpServletRequest request) {
        return authService.approve(id, userDTO,request);
    }

    /**
     * @desc    Untuk mendeteksi role dari akun yang sedang login
     * @route   POST /api/auth/check-role
     * @access  Public
     */
    @GetMapping("/check-role")
    public ResponseEntity<?> checkUserRole() {
        String role = authService.checkUserRole();
        return ResponseEntity.ok(role);
    }

}
