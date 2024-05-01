package com.sandeep.userservice.services;

import com.sandeep.userservice.exceptions.InvalidPasswordException;
import com.sandeep.userservice.exceptions.InvalidTokenException;
import com.sandeep.userservice.models.Role;
import com.sandeep.userservice.models.Token;
import com.sandeep.userservice.models.User;
import com.sandeep.userservice.repositories.TokenRepository;
import com.sandeep.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public User signUp(String email, String password, String name, List<Role> roles) {
        Optional<User> optionalUser= userRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            //user is already present in the db, so no need to signup
            return  optionalUser.get();
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public Token login(String email, String password) throws InvalidPasswordException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()) {
            //User with given email isn't present in DB.
            return null;
        }
        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword()))
        {
            throw new InvalidPasswordException("Please enter a correct password");
        }
        //Login successful, generate a new token.
        Token token = generateToken(user);
        return tokenRepository.save(token);
    }

    private Token generateToken(User user) {

        LocalDate thirtyDaysFromCurrentTime = LocalDate.now().plusDays(30);

        Date expiryDate = Date.from(thirtyDaysFromCurrentTime.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryDate);

        //Token value is randomly generated string of 128 characters.
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        return token;
    }

    @Override
    public void logout(String tokenValue) throws InvalidTokenException {

        //Validate whether the given token present in the DB or not as well as is_deleted is false.
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(tokenValue, false);
        if(optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid token passed");
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    @Override
    public User validateToken(String tokenValue) throws InvalidTokenException {
        Optional<Token> token = tokenRepository.findByValueAndDeleted(tokenValue, false);
        if(token.isEmpty()) {
            throw new InvalidTokenException("Invalid token passed");
        }
        return token.get().getUser();
    }
}
