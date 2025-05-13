package com.dangsim.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangsim.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByNickname(String nickname);

	boolean existsByNicknameAndIdNot(String nickname, Long id);
}
