package com.airline.auth.init;

import com.airline.auth.model.ERole;
import com.airline.auth.model.Role;
import com.airline.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
            System.out.println("Roles initialized successfully!");
            System.out.println("   - ROLE_USER");
            System.out.println("   - ROLE_ADMIN");
        } else {
            System.out.println("️Roles already exist in database. Skipping initialization.");
        }
    }
}