package com.airline.auth.controller;

import com.airline.auth.model.ERole;
import com.airline.auth.model.Role;
import com.airline.auth.model.User;
import com.airline.auth.payload.request.ChangePasswordRequest;
import com.airline.auth.payload.request.LoginRequest;
import com.airline.auth.payload.request.SignupRequest;
import com.airline.auth.payload.request.UpdateProfileRequest;
import com.airline.auth.payload.response.JwtResponse;
import com.airline.auth.payload.response.MessageResponse;
import com.airline.auth.repository.RoleRepository;
import com.airline.auth.repository.UserRepository;
import com.airline.auth.security.jwt.JwtUtils;
import com.airline.auth.security.service.UserDetailsImpl;
import com.airline.auth.validation.PasswordValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${security.password.expiry-days}")
    private long passwordExpiryDays;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails =
                (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Username not found"));

        long daysSinceChange = ChronoUnit.DAYS.between(
                user.getPasswordLastChanged(),
                LocalDateTime.now()
        );

        boolean passwordExpired = daysSinceChange >= passwordExpiryDays;

        String jwt = jwtUtils.generateJwtToken(authentication, passwordExpired);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new JwtResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        passwordExpired
                )
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }
        if (!PasswordValidator.isValid(signUpRequest.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(
                            "Password must be at least 6 characters long and contain at least one number"
                    ));
        }
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );
        user.setPasswordLastChanged(LocalDateTime.now());
        Set<Role> roles = new HashSet<>();
        if (signUpRequest.getRoles() == null || signUpRequest.getRoles().isEmpty()) {
            roles.add(roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found")));
        } else {
            for (String role : signUpRequest.getRoles()) {
                if ("admin".equalsIgnoreCase(role)) {
                    roles.add(roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role not found")));
                } else {
                    roles.add(roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Role not found")));
                }
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PutMapping("/user-profile")
    public ResponseEntity<?> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        if (userRepository.existsByUsername(request.getNewUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Username already taken"));
        }
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(request.getNewUsername());
        userRepository.save(user);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                UserDetailsImpl.build(user),
                null,
                currentUser.getAuthorities()
        );
        boolean passwordExpired = ChronoUnit.DAYS.between(
                user.getPasswordLastChanged(),
                LocalDateTime.now()
        ) >= passwordExpiryDays;
        String newToken = jwtUtils.generateJwtToken(newAuth, passwordExpired);
        return ResponseEntity.ok(
                new JwtResponse(
                        newToken,
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        currentUser.getAuthorities()
                                .stream()
                                .map(a -> a.getAuthority())
                                .toList(),
                        passwordExpired
                )
        );
    }

    @PutMapping("/user-change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Old password is incorrect"));
        }
        if (!PasswordValidator.isValid(request.getNewPassword())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(
                            "Password must be at least 6 characters long and contain at least one number"
                    ));
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        user.setPasswordLastChanged(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
    }
}
