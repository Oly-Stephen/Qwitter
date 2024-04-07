package com.twitter.twitterbackend.service;

import com.twitter.twitterbackend.exception.EmailAlreadyTakenException;
import com.twitter.twitterbackend.exception.UserDoesNotExistException;
import com.twitter.twitterbackend.models.ApplicationUser;
import com.twitter.twitterbackend.models.RegistrationObject;
import com.twitter.twitterbackend.models.Role;
import com.twitter.twitterbackend.repositories.RoleRepository;
import com.twitter.twitterbackend.repositories.UserRepository;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
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

    public ApplicationUser getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        try {
            return userRepository.save(user);
        }catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    public ApplicationUser registerUser(RegistrationObject ro) {
        ApplicationUser user = new ApplicationUser();

        user.setFirstName(ro.getFirstName());
        user.setLastName(ro.getLastName());
        user.setEmail(ro.getEmail());
        user.setDateOfBirth(ro.getDob());

        String name = user.getFirstName() + user.getLastName();

        boolean nameTaken = true;

        String tempName = "";

        while (nameTaken) {
            tempName = generateUsername(name);

            if(userRepository.findByUsername(tempName).isEmpty()) {
                nameTaken = false;
            }
        }

        user.setUsername(tempName);


        Set<Role> roles = user.getAuthorities();
        roles.add(roleRepository.findByAuthority("USER").get());
        user.setAuthorities(roles);

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    public void generateEmailVerification(String username) {

        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        user.setVerification(generateVerificationNumber());

        userRepository.save(user);
    }

    private String generateUsername(String name) {
        long generatedNumber = (long) Math.floor(Math.random() * 1_000_000_000);
        return name+generatedNumber;
    }

    private Long generateVerificationNumber() {
        return (long) Math.floor(Math.random() * 100_000_000);
    }
}
