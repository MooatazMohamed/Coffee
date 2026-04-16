package com.Coffee.controller.user;

import com.Coffee.dto.auth.login.LoginRequest;
import com.Coffee.dto.auth.signup.SignRequest;
import com.Coffee.dto.UserResponse;
import com.Coffee.handler.ApiResponse;
import com.Coffee.model.User;
import com.Coffee.service.security.UserService;
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

        if(!response.isSuccess()){
            return ResponseEntity.status(401).body(response);
        }

        return ResponseEntity.ok(response);
    }

}
