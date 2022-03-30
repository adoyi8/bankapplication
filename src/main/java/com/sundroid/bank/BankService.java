package com.sundroid.bank;

import com.sundroid.bank.appuser.AppUser;
import com.sundroid.bank.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class BankService {
    Map<String, Object> response = new HashMap<>();
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<Map<String, Object>> addAccount(AppUser newUser) {
        response = new HashMap<>();
        if (newUser.getName() == null || newUser.getName().length() < 1) {
            response.put("responseCode", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "Account name cannot be null");
            return ResponseEntity.badRequest().body(response);
        } else if (newUser.getPassword() == null || newUser.getPassword().length() < 1) {
            response.put("responseCode", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "Password cannot be null");
            return ResponseEntity.badRequest().body(response);
        } else if (newUser.getEmail() == null || newUser.getEmail().length() < 1) {
            response.put("responseCode", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "Email cannot be null");
            return ResponseEntity.badRequest().body(response);
        } else if (!Util.validateEmail(newUser.getEmail())) {
            response.put("responseCode", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "Enter valid email");
            return ResponseEntity.badRequest().body(response);
        } else {
            boolean userExists = isUserExist(newUser.getEmail());
            if (!userExists) {
                AccountModel accountModel = new AccountModel();
                accountModel.setBalance(0.0);
                accountModel.setAccountNumber(Util.generateAccountNumber());
                //  accountModel.setEmail(newUser.getEmail());
                System.out.println(accountModel);
                newUser.addAccount(accountModel);
                newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
                AppUser addedUser = appUserRepository.save(newUser);
                response.put("responseCode", HttpStatus.OK.value());
                response.put("success", true);
                response.put("accountName", addedUser.getName());
                response.put("accountNumber", addedUser.getAccounts().get(0).getAccountNumber());
                response.put("balance", addedUser.getAccounts().get(0).getBalance());
                response.put("message", "Account Successfully Created");
                return ResponseEntity.ok().body(response);
            } else {
                response.put("responseCode", HttpStatus.BAD_REQUEST.value());
                response.put("success", false);
                response.put("message", "User already exists");
                return ResponseEntity.badRequest().body(response);
            }
        }
    }

    public ResponseEntity<Map<String, Object>> withdraw(AppUser user, WithdrawalModel withdrawalModel) {
        Map<String, Object> response = new HashMap<>();
        AccountModel currentAccount = accountRepository.findByAccountNumber(user.getAccounts().get(0).getAccountNumber()).get();
        if (withdrawalModel.getAmount() > 1.0) {
            if (currentAccount.getBalance() > (withdrawalModel.getAmount())) {
                user.getAccounts().get(0).setBalance(currentAccount.getBalance() - withdrawalModel.getAmount());
                Map<String, Object> transaction = new HashMap<>();
                currentAccount.setBalance(currentAccount.getBalance() - withdrawalModel.getAmount());
                accountRepository.save(currentAccount);
                transaction.put("transactionDate", new Date());
                transaction.put("transactionType", "Withdrawal");
                transaction.put("narration", withdrawalModel.toString());
                transaction.put("amount", withdrawalModel.getAmount());
                transaction.put("accountBalance", currentAccount.getBalance());
                response.put("responseCode", HttpStatus.OK.value());
                response.put("success", true);
                response.put("message", "Withdrawal Successful");
                response.put("accountBalance", currentAccount.getBalance());
                return ResponseEntity.ok().body(response);
            } else {
                response.put("responseCode", HttpStatus.BAD_REQUEST.value());
                response.put("success", false);
                response.put("message", "Insufficient Funds!");
                return ResponseEntity.badRequest().body(response);
            }


        } else {
            response.put("responseCode", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "Withdrawal Amount must be greater that 1 naira");
            return ResponseEntity.badRequest().body(response);
        }
    }


    public ResponseEntity<Map<String, Object>> deposit(AppUser user, DepositModel depositModel) {
        Map<String, Object> response = new HashMap<>();
        AccountModel currentAccount = accountRepository.findByAccountNumber(user.getAccounts().get(0).getAccountNumber()).orElseThrow();
        if (depositModel.getAmount() > 1.0) {
            currentAccount.setBalance(currentAccount.getBalance() + depositModel.getAmount());
            Map<String, Object> transaction = new HashMap<>();
            transaction.put("transactionDate", new Date());
            transaction.put("transactionType", "Deposit");
            transaction.put("narration", depositModel.toString());
            transaction.put("amount", depositModel.getAmount());
            transaction.put("accountBalance", currentAccount.getBalance());
            response.put("responseCode", HttpStatus.OK.value());
            response.put("success", true);
            response.put("message", "Deposit Successful");
            response.put("accountBalance", currentAccount.getBalance());
            accountRepository.save(currentAccount);
            return ResponseEntity.ok().body(response);

        } else {
            response.put("responseCode", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "Deposit Amount must be greater that 1 naira");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getAccountInfo(AppUser appUser) {
        Map<String, Object> response = new HashMap<>();

        AccountModel currentAccount = appUser.getAccounts().get(0);
            Map<String, Object> accountMap = new HashMap<>();
            accountMap.put("accountName", appUser.getName());
            accountMap.put("accountNumber", currentAccount.getAccountNumber());
            accountMap.put("balance", currentAccount.getBalance());
            response.put("responseCode", HttpStatus.OK.value());
            response.put("success", true);
            response.put("message", "");
            response.put("account", accountMap);
            return ResponseEntity.ok().body(response);

    }


    @Transactional
    public ResponseEntity<Map<String, Object>> transfer(AppUser user, TransactionModel transactionModel) {
        Map<String, Object> response = new HashMap<>();
        if (transactionModel.getAmount() == null) {
            response.put("responseCode", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "Transaction amount must not be empty");
            return ResponseEntity.badRequest().body(response);
        } else if (transactionModel.getDestinationAccountNumber() == null || transactionModel.getDestinationAccountNumber().length() < 1) {
            response.put("responseCode", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("message", "Destination Account must not be empty");
            return ResponseEntity.badRequest().body(response);
        } else {
            Optional<AccountModel> destinationAccount = accountRepository.findByAccountNumber(transactionModel.getDestinationAccountNumber());
            Optional<AccountModel> senderAccount = accountRepository.findByAccountNumber(user.getAccounts().get(0).getAccountNumber());

            if (!destinationAccount.isPresent()) {
                response.put("responseCode", HttpStatus.BAD_REQUEST.value());
                response.put("success", false);
                response.put("message", "Destination Account Number does not exist!");
                return ResponseEntity.badRequest().body(response);
            } else if (transactionModel.getAmount() < 1.0) {
                response.put("responseCode", HttpStatus.BAD_REQUEST.value());
                response.put("success", false);
                response.put("message", "Transfer Amount must be greater than 1 Naira");
                return ResponseEntity.badRequest().body(response);
            } else if (senderAccount.get().getBalance() < transactionModel.getAmount()) {
                response.put("responseCode", HttpStatus.BAD_REQUEST.value());
                response.put("success", false);
                response.put("message", "Insufficient Funds!");
                return ResponseEntity.badRequest().body(response);
            } else {
                senderAccount.get().setBalance(senderAccount.get().getBalance() - transactionModel.getAmount());
                destinationAccount.get().setBalance(senderAccount.get().getBalance() + transactionModel.getAmount());
                transactionModel.setTransactionDate(new Date());
                transactionModel.setSenderId(senderAccount.get().getEmail());
                transactionModel.setDestinationId(destinationAccount.get().getEmail());
                transactionModel.setStatus(TransactionStatus.SUCCESSFUL);
                transactionModel.setSenderAccountNumber(senderAccount.get().getAccountNumber());
                accountRepository.save(senderAccount.get());
                accountRepository.save(destinationAccount.get());
                response.put("responseCode", HttpStatus.OK.value());
                response.put("success", true);
                response.put("message", "Transaction Successful!");
                transactionRepository.save(transactionModel);
                return ResponseEntity.ok().body(response);
            }
        }
    }

    public ResponseEntity<List<TransactionModel>> getAllTransactions(Optional<FilterModel> request) {
        List<TransactionModel> allTransactions = transactionRepository.findAll();
        if (request.isPresent()) {
            FilterModel filterModel = request.get();
            if (filterModel != null) {
                if (filterModel.getUserId() != null && filterModel.getUserId().length() > 0) {
                    allTransactions = allTransactions.stream().filter(i -> (i.getDestinationId().equalsIgnoreCase(filterModel.getUserId()) || i.getSenderId().equalsIgnoreCase(filterModel.getUserId()))).collect(Collectors.toList());
                }
                if (filterModel.getStatus() != null && filterModel.getStatus().length() > 0) {
                    allTransactions = allTransactions.stream().filter(i -> (i.getStatus().name().equalsIgnoreCase(filterModel.getStatus()))).collect(Collectors.toList());
                }
                if (filterModel.getStartDate() != null) {
                    allTransactions = allTransactions.stream().filter(i -> (i.getTransactionDate().after(filterModel.getStartDate()))).collect(Collectors.toList());
                }
                if (filterModel.getEndDate() != null) {
                    allTransactions = allTransactions.stream().filter(i -> (i.getTransactionDate().before(filterModel.getEndDate()))).collect(Collectors.toList());
                }

            }
        }
        return ResponseEntity.ok().body(allTransactions);
    }

    public ResponseEntity<List<TransactionModel>> todayTransactions() {
        List<TransactionModel> allTransactions = transactionRepository.findAll();
        allTransactions = allTransactions.stream().filter(i -> i.getTransactionDate().before(new Date())).collect(Collectors.toList());
        return ResponseEntity.ok().body(allTransactions);
    }

    public boolean isUserExist(String email) {
        boolean userExists = appUserRepository
                .findByEmail(email)
                .isPresent();
        return userExists;
    }

    @Scheduled(cron = "1 * * * * ?")
    public void scheduleTaskUsingCronExpression() {
        System.out.println("Cron called");
        todayTransactions();
        computeCommissions();
    }

    @Transactional
    public void computeCommissions() {
        List<TransactionModel> allTransactions = transactionRepository.findAll().stream().filter(i -> (i.getStatus().equals(TransactionStatus.SUCCESSFUL) && !i.isCommissioned())).collect(Collectors.toList());
        for (TransactionModel transaction : allTransactions) {
            Double transactionFee = transaction.getAmount() * 0.005;
            if (transactionFee > 100) {
                transactionFee = 100.0;
            }
            Double commission = transactionFee * 0.20;
            transaction.setCommission(commission);
            transaction.setTransactionFee(transactionFee);
            transaction.setCommissioned(true);
            transactionRepository.save(transaction);

        }
    }
}
