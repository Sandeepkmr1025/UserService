package com.sandeep.userservice.services;

import com.sandeep.userservice.exceptions.InvalidPasswordException;
import com.sandeep.userservice.exceptions.InvalidTokenException;
import com.sandeep.userservice.models.Role;
import com.sandeep.userservice.models.Token;
import com.sandeep.userservice.models.User;

import java.util.List;

public interface IUserService {
    User signUp(String email, String password, String name, List<Role> roles);

    Token login(String email, String password) throws InvalidPasswordException;

    void logout(String token) throws InvalidTokenException;

    User validateToken(String tokenValue) throws InvalidTokenException;
}
