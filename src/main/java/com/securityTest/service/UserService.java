package com.securityTest.service;

import com.securityTest.dto.LoginRequest;
import com.securityTest.dto.SignRequest;
import com.securityTest.dto.UserResponse;
import com.securityTest.handler.ApiResponse;
import com.securityTest.handler.EmailAlreadyExistsException;
import com.securityTest.mapper.UserMapper;
import com.securityTest.model.Role;
import com.securityTest.model.User;
import com.securityTest.repository.RoleRepository;
import com.securityTest.repository.UserRepository;
import com.securityTest.service.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserResponse save(SignRequest signRequest){
        User existingEmail = userRepository.findByEmail(signRequest.getEmail());
        if(existingEmail!=null){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        User user = userMapper.toEntity(signRequest);
        user.setRole(userRole);
        user.setPassword(encoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public ApiResponse<?> login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail());

        if(user == null || !encoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );

        Map<String, String> data = new HashMap<>();
        data.put("token", token);

        return ApiResponse.success("Login successful", data);
    }
}
