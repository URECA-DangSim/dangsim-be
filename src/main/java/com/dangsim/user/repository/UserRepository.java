package com.dangsim.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dangsim.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByNickname(String nickname);
}
