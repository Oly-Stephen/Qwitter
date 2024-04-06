package com.twitter.twitterbackend.service;

import com.twitter.twitterbackend.models.ApplicationUser;
import com.twitter.twitterbackend.models.Role;
import com.twitter.twitterbackend.repositories.RoleRepository;
import com.twitter.twitterbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public ApplicationUser registerUser(ApplicationUser user) {
        Set<Role> roles = user.getAuthorities();
        roles.add(roleRepository.findByAuthority("USER").get());
        user.setAuthorities(roles);

        return userRepository.save(user);
    }
}
