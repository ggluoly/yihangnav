package com.yihangnav.web.controller;

import com.yihangnav.core.domain.UserAccount;
import com.yihangnav.core.service.UserService;
import com.yihangnav.security.JwtTokenProvider;
import com.yihangnav.web.dto.ApiResponse;
import com.yihangnav.web.dto.AuthResponse;
import com.yihangnav.web.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Validated @RequestBody LoginRequest request,
                                                           HttpServletRequest servletRequest,
                                                           @RequestHeader(value = "User-Agent", required = false) String ua) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(request.getUsername());
        userService.findByUsername(request.getUsername()).ifPresent(user ->
                userService.recordLogin(user, servletRequest.getRemoteAddr(), ua));
        UserAccount user = userService.findByUsername(request.getUsername()).orElse(null);
        String role = user != null ? user.getRole() : "USER";
        return ResponseEntity.ok(ApiResponse.ok(new AuthResponse(token, request.getUsername(), role)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserAccount>> me() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return ResponseEntity.status(401).body(ApiResponse.fail("unauthorized"));
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(ApiResponse.ok(user)))
                .orElseGet(() -> ResponseEntity.status(401).body(ApiResponse.fail("unauthorized")));
    }
}
