package com.dangsim.token.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dangsim.token.entity.Token;
import com.dangsim.user.entity.User;

public interface TokenRepository extends JpaRepository<Token, Long> {
	Optional<Token> findByUser(User user);

	void deleteByUser(User user);
}
