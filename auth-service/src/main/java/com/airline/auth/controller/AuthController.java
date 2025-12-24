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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        user.setPasswordLastChanged(LocalDateTime.now());
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    //adding endpoint for fetching user name , email , and roles
    // adding endpoint to update username
    // adding endpoint to update password
    @PutMapping("/user-profile")
    public ResponseEntity<?> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        String newUsername = request.getNewUsername();
        if (userRepository.existsByUsername(newUsername)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Username already taken"));
        }
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(newUsername);
        userRepository.save(user);
        //  regenerate authentication + JWT
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                UserDetailsImpl.build(user),
                null,
                currentUser.getAuthorities()
        );
        String newToken = jwtUtils.generateJwtToken(newAuth);
        return ResponseEntity.ok(new JwtResponse(
                newToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                currentUser.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .toList()
        ));
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
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(
                new MessageResponse("Password updated successfully")
        );
    }


}