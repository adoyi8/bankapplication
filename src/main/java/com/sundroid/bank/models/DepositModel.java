package com.sundroid.bank.models;

public class DepositModel {

    Double amount;

    public DepositModel() {
    }



    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Deposited " + amount + " into account";
    }
}
