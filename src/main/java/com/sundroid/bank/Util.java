package com.sundroid.bank;

import com.sundroid.bank.models.AccountModel;
import org.springframework.boot.SpringApplication;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {

    public static String generateAccountNumber(){

        Random rnd = new Random();
        int n = 1000000000 + rnd.nextInt(900000000);
        final String accountNumber =  String.valueOf(n);
        return accountNumber;

    }
    public static  String generateAccessToken(){

        return UUID.randomUUID().toString();

    }
    public static boolean validateEmail(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}
