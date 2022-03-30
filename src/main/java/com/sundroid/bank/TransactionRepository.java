package com.sundroid.bank;

import com.sundroid.bank.models.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {

}
