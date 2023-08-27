package com.awesomegic.service;

import com.awesomegic.AwesomegicValidationHelper;
import com.awesomegic.model.Account;
import com.awesomegic.model.Transaction;
import com.awesomegic.repository.AccountRepository;
import com.awesomegic.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService extends  AbstractService{

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final AwesomegicValidationHelper awesomegicValidationHelper;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AwesomegicValidationHelper awesomegicValidationHelper, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.awesomegicValidationHelper = awesomegicValidationHelper;
        this.accountRepository = accountRepository;
    }


    public void addTransaction(String transactionDate, BigDecimal transactionAmount,
                               String transactionType, Account account) {
        LocalDate tranDate = awesomegicValidationHelper.convertLocalDate(transactionDate);
        Transaction transaction = new Transaction(tranDate,transactionAmount,transactionType,account.getAccountNumber(),account.getBalance());
        List<Transaction> transactionList = this.transactionRepository.findByTransactionDateOrderByTransactionDateDesc(tranDate,account.getAccountNumber(),
                Sort.by(Sort.Direction.DESC,"transactionDate"));
        String transactionId = retreriveTransactionId(transactionList);
        transaction.setTransactionId(transactionId);
        if(awesomegicValidationHelper.validateTransaction(transaction)) {
            transactionRepository.save(transaction);
        }else{
            super.printInvalidMessage("Invalid input for transaction. Please try again.");
        }
    }

    private String retreriveTransactionId(List<Transaction> transactionList) {
        if(CollectionUtils.isEmpty(transactionList)) {
            return awesomegicValidationHelper.generateTransactionId(null);
        }
        return awesomegicValidationHelper.generateTransactionId(transactionList.get(0).getTransactionId());
    }

    public void printStatement(String line) {
        String[] acctArray = line.split("\\|");
        if(acctArray == null || acctArray.length != 2) {
            super.printInvalidMessage("Invalid input. Please try again.");
            return;
        }
        if(!awesomegicValidationHelper.validateAccountNumber(acctArray[0])) {
            super.printInvalidMessage("Invalid account number. Please try again.");
            if(this.accountRepository.findByAccountNumber(acctArray[0]) == null) {
                super.printInvalidMessage("Account number does not exist. Please try again.");
            }
            return;
        }
        if(!awesomegicValidationHelper.validateMonth(acctArray[1])) {
            super.printInvalidMessage("Invalid month. Please try again.");
            return;
        }
        Integer month = Integer.valueOf(acctArray[1]);
        LocalDate startDayMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDate lastDayMonth = LocalDate.of(LocalDate.now().getYear(), month, startDayMonth.lengthOfMonth());
        String accountNo = acctArray[0];

        List<Transaction> transactionList = transactionRepository.
                findByTransactionDateBetween(startDayMonth, lastDayMonth, accountNo);
        if(!CollectionUtils.isEmpty(transactionList)) {
            System.out.println("AwesomeGIC Bank Statement");
            System.out.println("Account No : " + accountNo);
            System.out.println("Date     | Txn Id      | Type | Amount | Balance |");
            transactionList.forEach(transaction -> {
                System.out.println(awesomegicValidationHelper.convertDateToString(transaction.getTransactionDate()) + " | "
                        + transaction.getTransactionId() + " | " + transaction.getTransactionType() + " | "
                        + transaction.getTransactionAmount() + " | " + transaction.getBalance());
            });
            System.out.println("Thank you for using AwesomeGIC Bank!");
            System.exit(0);
        }
    }

}
