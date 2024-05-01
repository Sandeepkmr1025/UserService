package com.sandeep.userservice.repositories;

import com.sandeep.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    //Optional
    @Override
    Token save(Token token);

    //select * from token where value={} and is_deleted = false;
    Optional<Token> findByValueAndDeleted(String value, boolean isDeleted);
}
