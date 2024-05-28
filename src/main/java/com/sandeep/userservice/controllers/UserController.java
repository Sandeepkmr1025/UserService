package com.sandeep.userservice.controllers;

import com.sandeep.userservice.dtos.*;
import com.sandeep.userservice.exceptions.InvalidPasswordException;
import com.sandeep.userservice.exceptions.InvalidTokenException;
import com.sandeep.userservice.models.Token;
import com.sandeep.userservice.models.User;
import com.sandeep.userservice.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto)
    {
        User user = userService.signUp(requestDto.getEmail(), requestDto.getPassword(), requestDto.getName(), requestDto.getRoles());
        return UserDto.fromUser(user);
    }

    @PostMapping("/login")
    public LogInResponseDto logIn(@RequestBody LogInRequestDto requestDto) throws InvalidPasswordException {
        Token token = userService.login(requestDto.getEmail(), requestDto.getPassword());
        LogInResponseDto responseDto = new LogInResponseDto();
        responseDto.setToken(token);
        return responseDto;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogOutRequestDto requestDto) {

        ResponseEntity<Void> responseEntity = null;
        try {
            userService.logout(requestDto.getToken());
            responseEntity = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Something went wrong");
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) throws InvalidTokenException {
        User user = userService.validateToken(token);
        return UserDto.fromUser(user);
    }

    @GetMapping("/{id}")
    public String getUserDetails(@PathVariable long id) {
        System.out.println("Received request to retrieve user details");
        return "Hello from UserService! with id : "+id;
    }

}
