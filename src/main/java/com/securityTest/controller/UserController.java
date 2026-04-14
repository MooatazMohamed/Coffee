package com.securityTest.controller;

import com.securityTest.dto.LoginRequest;
import com.securityTest.dto.SignRequest;
import com.securityTest.dto.UserResponse;
import com.securityTest.handler.ApiResponse;
import com.securityTest.model.User;
import com.securityTest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody @Valid SignRequest request){

        UserResponse response = userService.save(request);

        return ResponseEntity.ok(
                ApiResponse.success("User registered successfully", response)
        );
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest){
        ApiResponse<?> response = userService.login(loginRequest);

        return ResponseEntity.ok(response);
    }

}
