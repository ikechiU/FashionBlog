package com.example.blog;

import com.example.blog.entities.Authority;
import com.example.blog.entities.Role;
import com.example.blog.entities.User;
import com.example.blog.repositories.AuthorityRepository;
import com.example.blog.repositories.RoleRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.shared.utils.Authorities;
import com.example.blog.shared.utils.Roles;
import com.example.blog.shared.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class InitialUserSetUp {

    private final AuthorityRepository authorityRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Utils utils;

    private final UserRepository userRepository;


    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("From Application ready event....");

        Authority readAuthority = createAuthority(Authorities.READ_AUTHORITY.name());
        Authority writeAuthority = createAuthority(Authorities.WRITE_AUTHORITY.name());
        Authority deleteAuthority = createAuthority(Authorities.DELETE_AUTHORITY.name());

        createRole(Roles.ROLE_USER.name(), Collections.singletonList(readAuthority));
        Role roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

        if (roleAdmin == null) return;

        User adminUser = new User();
        adminUser.setFirstname("Ikechi");
        adminUser.setLastname("Ucheagwu");
        adminUser.setFirstname("Ikechi");
        adminUser.setEmail("ikechi@admin.com");
        adminUser.setPhoneNumber("ikechi@admin.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateUserId(10));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("123456789"));
        adminUser.setRoles(List.of(roleAdmin));

        User storedUser = userRepository.findByEmail("ikechi@admin.com").orElse(null);
        if(storedUser == null) {
            userRepository.save(adminUser);
        }

    }
    @Transactional
    private Authority createAuthority(String authorityName) {
        Authority authority = authorityRepository.findByName(authorityName);
        if (authority == null) {
            authority = new Authority(authorityName);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    private Role createRole(String roleName, List<Authority> authorities) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role(roleName);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }

}
