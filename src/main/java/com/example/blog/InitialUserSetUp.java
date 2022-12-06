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
        Authority privilegeAuthority = createAuthority(Authorities.PRIVILEGE_AUTHORITY.name());
        Authority deleteAuthority = createAuthority(Authorities.DELETE_AUTHORITY.name());

        createRole(Roles.ROLE_USER.name(), Collections.singletonList(readAuthority));
        Role roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority));
        Role roleSuperAdmin = createRole(Roles.ROLE_SUPER_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, privilegeAuthority, deleteAuthority));

        if (roleAdmin == null) return;

        User adminUser = new User();
        adminUser.setFirstname("admin");
        adminUser.setLastname("admin");
        adminUser.setFirstname("admin");
        adminUser.setEmail("admin@admin.com");
        adminUser.setPhoneNumber("admin@admin.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateUserId(10));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
        adminUser.setRoles(List.of(roleAdmin));

        User storedUser = userRepository.findByEmail("admin@admin.com").orElse(null);
        if(storedUser == null) {
            userRepository.save(adminUser);
        }


        if (roleSuperAdmin == null) return;

        User superAdminUser = new User();
        superAdminUser.setFirstname("Super");
        superAdminUser.setLastname("Super");
        superAdminUser.setFirstname("Super");
        superAdminUser.setEmail("super@admin.com");
        superAdminUser.setPhoneNumber("super@admin.com");
        superAdminUser.setEmailVerificationStatus(true);
        superAdminUser.setUserId(utils.generateUserId(10));
        superAdminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("123456789"));
        superAdminUser.setRoles(List.of(roleAdmin));

        User storedSuperUser = userRepository.findByEmail("super@admin.com").orElse(null);
        if(storedSuperUser == null) {
            userRepository.save(superAdminUser);
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
