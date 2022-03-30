package com.sundroid.bank.models;


import com.sundroid.bank.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccountModel {


    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private Long id;
    Double balance;
    Double initialDeposit;
    String accountNumber;
    String email;







    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(Double initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "id=" + id +
                ", balance=" + balance +
                ", initialDeposit=" + initialDeposit +
                ", accountNumber='" + accountNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
