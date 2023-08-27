package com.awesomegic.service;

import com.awesomegic.AwesomegicValidationHelper;
import com.awesomegic.model.Account;
import com.awesomegic.repository.AccountRepository;
import com.awesomegic.util.TransactionTypeEnum;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

@Service
public class AccountService extends  AbstractService{

    private final AccountRepository accountRepository;

    private final TransactionService transactionService;

    private final AwesomegicValidationHelper awesomegicValidationHelper;

    @Autowired
    public AccountService(AccountRepository accountRepository , TransactionService transactionService, AwesomegicValidationHelper awesomegicValidationHelper) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.awesomegicValidationHelper = awesomegicValidationHelper;
    }

    public void addAcount(String accountLine) {
        String[] acctArray = accountLine.split("\\|");
        System.out.println(acctArray.length);
        if(acctArray == null || acctArray.length != 4) {
            super.printInvalidMessage("Invalid input. Please try again.");
            return;
        }
        String transactionDate = acctArray[0];
        String acctNumber = acctArray[1];
        String transactionType = acctArray[2];
        //Validate date done in AwesomegicValidationHelper
        awesomegicValidationHelper.convertLocalDate(transactionDate);
        if(!awesomegicValidationHelper.validateAccountNumber(acctNumber)) {
            super.printInvalidMessage("Invalid account number. Please try again.");
            return;
        }
        if(!awesomegicValidationHelper.validateAmount(acctArray[3])) {
            super.printInvalidMessage("Invalid amount. Please try again.");
            return;
        }
        BigDecimal transactionAmount = new BigDecimal(acctArray[3]);
        Account account = accountRepository.findByAccountNumber(acctNumber);
        if(account == null) {
            account = new Account(acctNumber);
        }
        transactionType = TransactionTypeEnum.getTransactiionEnum(transactionType).code;
        System.out.println("Transaction type: " + transactionType);
        account.calculateBalance(transactionAmount , transactionType);
        accountRepository.save(account);
        transactionService.addTransaction(transactionDate,transactionAmount,transactionType,account);
        String line = acctNumber+"|"+ LocalDate.now().getMonthValue();
        transactionService.printStatement(line);

    }
}
