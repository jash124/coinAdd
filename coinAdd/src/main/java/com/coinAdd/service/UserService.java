package com.coinAdd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.coinAdd.dto.UpdateRequest;
import com.coinAdd.entity.User;
import com.coinAdd.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

	
	public BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	 

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && bCryptPasswordEncoder.matches(password, userOpt.get().getPassword())) {
            return userOpt;
        }
        return Optional.empty();
    }
    
    public Optional<User> updateUser(User user, UpdateRequest updateRequest) {
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setMobile(updateRequest.getMobile());
        user.setPassword(bCryptPasswordEncoder.encode(updateRequest.getPassword()));
        return Optional.of(userRepository.save(user));
    }
}