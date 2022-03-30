package com.sundroid.bank;


import com.sundroid.bank.appuser.AppUser;
import com.sundroid.bank.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class MainController {
    @Autowired
    BankService accountService;
    @PostMapping("/create_account")
    public ResponseEntity<Map<String, Object>> createAccount(@RequestBody AppUser request){
      return accountService.addAccount(request);
    }
    @PostMapping("/withdrawal")
    public ResponseEntity<Map<String, Object>> withdrawal(@AuthenticationPrincipal AppUser user, @RequestBody WithdrawalModel request){
          return accountService.withdraw(user,request);
    }
    @PostMapping("/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@AuthenticationPrincipal AppUser user,@RequestBody DepositModel request){
        return accountService.deposit(user,request);
    }
    @GetMapping("/account_info")
    public ResponseEntity<Map<String,Object>> getAccountInfo(@AuthenticationPrincipal AppUser user) {
        return accountService.getAccountInfo(user);
    }
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@AuthenticationPrincipal AppUser user,@RequestBody TransactionModel request){
        return accountService.transfer(user,request);
    }
    @GetMapping("/all_transactions")
    public ResponseEntity<List<TransactionModel>> transactions(@RequestBody Optional<FilterModel> request){
        return accountService.getAllTransactions(request);
    }
    @GetMapping("/today_transactions")
    public ResponseEntity<List<TransactionModel>> todayTransactions(){
        return accountService.todayTransactions();
    }

}
