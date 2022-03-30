package com.sundroid.bank;

import com.sundroid.bank.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByEmail(String s);
}
