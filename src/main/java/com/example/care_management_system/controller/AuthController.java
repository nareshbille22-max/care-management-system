package com.example.care_management_system.controller;

import com.example.care_management_system.dto.AuthRequest;
import com.example.care_management_system.dto.JwtResponse;
import com.example.care_management_system.entity.User;
import com.example.care_management_system.repository.UserRepository;
import com.example.care_management_system.security.JwtUtil;
import com.example.care_management_system.service.EmailService;
import com.example.care_management_system.service.MyUserDetailsService;
import com.example.care_management_system.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws MessagingException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        emailService.sendEmail(user.getEmail(), user.getName(), "register", "");

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthRequest authRequest, HttpServletRequest request) throws BadCredentialsException, MessagingException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw e;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        final Date expirationDate = jwtUtil.extractExpiration(jwt);
        long expiryMillis = expirationDate.getTime();

        JwtResponse response = new JwtResponse(jwt, expiryMillis);

        Optional<User> user = userRepository.findByEmail(jwtUtil.extractUsername(jwt));

        emailService.sendEmail(authRequest.getEmail(), user.get().getName(), "login", "");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> createResetPasswordToken(@Valid @RequestParam String emailId) throws BadCredentialsException, MessagingException {

        Optional<User> user = userRepository.findByEmail(emailId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("No user found with this email address");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(emailId);
        String jwt = jwtUtil.generateToken2(userDetails);
        Date expirationDate = jwtUtil.extractExpiration(jwt);
        long expiryMillis = expirationDate.getTime();

        JwtResponse response = new JwtResponse(jwt, expiryMillis);

        String resetUrl = "http://192.168.1.18:8080/reset-password?token=" + jwt;

        emailService.sendEmail(emailId, user.get().getName(), "forgot-password", resetUrl);

        return ResponseEntity.ok("Password reset link sent to your email");

    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {

        String email = jwtUtil.extractUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!jwtUtil.validateToken(token, userDetails)) {
            return ResponseEntity.badRequest().body("Invalid or expired reset token!");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful");
    }


}
