package com.sundroid.bank.security;

import com.sundroid.bank.appuser.AppUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


@Configuration
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}