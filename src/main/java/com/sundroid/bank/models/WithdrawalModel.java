package com.sundroid.bank.models;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WithdrawalModel {

    private Double amount;
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return amount + " Was withdrawn from ";
    }
}
