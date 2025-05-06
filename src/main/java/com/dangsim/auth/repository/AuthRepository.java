package com.dangsim.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangsim.auth.entity.Auth;
import com.dangsim.auth.entity.AuthProvider;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
	Optional<Auth> findByProviderAndProviderId(AuthProvider provider, String providerId);
}
