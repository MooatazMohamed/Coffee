package com.Coffee.service.security;

import com.Coffee.dto.auth.login.LoginRequest;
import com.Coffee.dto.auth.signup.SignRequest;
import com.Coffee.dto.UserResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.handler.EmailAlreadyExistsException;
import com.Coffee.mapper.user.UserMapper;
import com.Coffee.model.Role;
import com.Coffee.model.User;
import com.Coffee.repository.user.RoleRepository;
import com.Coffee.repository.user.UserRepository;
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
// sign up
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
            return ApiResponse.error("Login failed", "Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getBranch().getId()
        );

        Map<String, String> data = new HashMap<>();
        data.put("token", token);

        return ApiResponse.success("Login successful", data);
    }
}
