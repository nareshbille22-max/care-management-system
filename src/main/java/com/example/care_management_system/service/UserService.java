package com.example.care_management_system.service;

import com.example.care_management_system.entity.User;
import com.example.care_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String getNameByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getName)
                .orElse("Name not found");
    }

}
