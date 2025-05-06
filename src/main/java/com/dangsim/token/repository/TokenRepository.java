package com.dangsim.token.repository;

import com.dangsim.token.entity.Token;
import com.dangsim.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser(User user);
    void deleteByUser(User user);
}
