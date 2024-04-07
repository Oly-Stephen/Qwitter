package com.twitter.twitterbackend.controller;

import com.twitter.twitterbackend.exception.EmailAlreadyTakenException;
import com.twitter.twitterbackend.exception.UserDoesNotExistException;
import com.twitter.twitterbackend.models.ApplicationUser;
import com.twitter.twitterbackend.models.RegistrationObject;
import com.twitter.twitterbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler({EmailAlreadyTakenException.class})
    public ResponseEntity<String> handleEmailTaken() {
        return new ResponseEntity<String>("The email you provided is already in use", HttpStatus.CONFLICT);
    }

    // http://localhost:8080/auth/register
    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationObject ro) {
        return userService.registerUser(ro);
    }

    @ExceptionHandler({UserDoesNotExistException.class})
    public ResponseEntity<String> handleUSerDoesntExist() {
        return new ResponseEntity<String>("The user you are looking for does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/phone")
    public ApplicationUser updatePhoneNumber(@RequestBody LinkedHashMap<String, String> body) {
        String username = body.get("username");
        String phone  = body.get("phone");

        ApplicationUser user = userService.getUserByUsername(username);

        user.setPhone(phone);

        return userService.updateUser(user);
    }

    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String, String> body) {
        userService.generateEmailVerification(body.get("username"));

        return new ResponseEntity<String>("Verification code generated, email sent", HttpStatus.OK);
    }
}
