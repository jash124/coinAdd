package com.coinAdd.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.coinAdd.dto.LoginRequest;
import com.coinAdd.dto.UpdateRequest;
import com.coinAdd.entity.ApiRequestResponse;
import com.coinAdd.entity.User;
import com.coinAdd.service.CryptoService;
import com.coinAdd.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
    private CryptoService cryptoService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Validated @RequestBody User user, @RequestHeader HttpHeaders headers) {
        if (!headers.containsKey("X-Confirm-Origin") || !headers.get("X-Confirm-Origin").contains("ExpectedValue")) {
            return new ResponseEntity<>("Invalid header value", HttpStatus.FORBIDDEN);
        }

        if (userService.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email is already in use", HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>("Username is already in use", HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
	
	 @PostMapping("/login")
	    public ResponseEntity<String> login(@Validated @RequestBody LoginRequest loginRequest, @RequestHeader HttpHeaders headers) {
	        if (!headers.containsKey("X-Confirm-Origin") || !headers.get("X-Confirm-Origin").contains("ExpectedValue")) {
	            return new ResponseEntity<>("Invalid header value", HttpStatus.FORBIDDEN);
	        }

	        Optional<User> userOpt = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
	        if (userOpt.isPresent()) {
	            return new ResponseEntity<>("Login successful", HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
	        }
	    }
	 
	 
	 	@PostMapping("/update")
	 	public ResponseEntity<String> update(@Validated @RequestBody UpdateRequest updateRequest, @RequestHeader HttpHeaders headers) {
		       System.out.println("The request is coming");
			 if (!headers.containsKey("X-Confirm-Origin") || !headers.get("X-Confirm-Origin").contains("ExpectedValue")) {
		            return new ResponseEntity<>("Invalid header value", HttpStatus.FORBIDDEN);
		        }

			 System.out.println("The request is coming2");
		        String email = SecurityContextHolder.getContext().getAuthentication().getName();
		        Optional<User> userOpt = userService.authenticateUser(email, updateRequest.getPassword());

		        if (userOpt.isPresent()) {
		            User user = userOpt.get();
		            userService.updateUser(user, updateRequest);
		            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
		        } else {
		            return new ResponseEntity<>("User not found or invalid password", HttpStatus.NOT_FOUND);
		        }
		    }
	 	
	 	@GetMapping("/api/crypto/quotes")
	    public ResponseEntity<ApiRequestResponse> getCryptoQuotes(@RequestParam Long userId, @RequestHeader HttpHeaders headers) {
	        if (!headers.containsKey("X-Confirm-Origin") || !headers.get("X-Confirm-Origin").contains("ExpectedValue")) {
	            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	        }

	        ApiRequestResponse response = cryptoService.getCryptoQuotes(userId);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	   
	 	
}
