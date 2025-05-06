package com.dangsim.auth.repository;

import com.dangsim.auth.entity.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangsim.auth.entity.Auth;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByProviderAndProviderId(AuthProvider provider, String providerId);
}
