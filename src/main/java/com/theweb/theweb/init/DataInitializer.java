package com.theweb.theweb.init;

import com.theweb.theweb.domain.Role;
import com.theweb.theweb.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.findByRoleName("ROLE_USER") == null) {
            roleRepository.save(Role.builder().roleName("ROLE_USER").build());
        }
        if (roleRepository.findByRoleName("ROLE_ADMIN") == null) {
            roleRepository.save(Role.builder().roleName("ROLE_ADMIN").build());
        }
    }
}
