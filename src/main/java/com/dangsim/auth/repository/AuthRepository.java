package com.dangsim.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangsim.auth.entity.Auth;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
}
