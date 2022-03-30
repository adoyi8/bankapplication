package com.sundroid.bank;

import com.sundroid.bank.models.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountModel, Long> {
    Optional<AccountModel> findByAccountNumber(String s);
}
