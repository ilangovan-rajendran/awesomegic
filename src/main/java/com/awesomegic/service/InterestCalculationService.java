package com.awesomegic.service;

import com.awesomegic.model.Account;
import com.awesomegic.model.InterestRule;
import com.awesomegic.model.Transaction;
import com.awesomegic.repository.AccountRepository;
import com.awesomegic.repository.InterestRuleRepository;
import com.awesomegic.repository.TransactionRepository;
import com.awesomegic.util.TransactionTypeEnum;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterestCalculationService {

    private InterestRuleRepository interestRuleRepository;
    private TransactionRepository   transactionRepository;

    private Map<String, String> interestRuleMap = new HashMap<>();

    private AccountRepository accountRepository;

    @Autowired
    public InterestCalculationService(InterestRuleRepository interestRuleRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.interestRuleRepository = interestRuleRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }


    @PostConstruct
    public void init() {
        interestRuleMap.put("1-14" , "RULE02");
        interestRuleMap.put("15-25" , "RULE03");
        interestRuleMap.put("26-30" , "RULE03");
    }

    public void calculateInterest() {
        System.out.println("Calculating interest for all accounts...");
        List<Account> accounts = accountRepository.findAll();
        accounts.forEach(account -> {
            BigDecimal totalInterest = BigDecimal.ZERO;
            interestRuleMap.forEach((k , v) -> {
                System.out.println("Calculating interest for account: " + account.getAccountNumber() + " for rule: " + v);
                List<InterestRule> interestRules = interestRuleRepository.findByRuleId(v);
                if(interestRules.isEmpty()) {
                    System.out.println("No interest rule found for rule id: " + v);
                    return;
                }
                InterestRule interestRule = interestRules.get(0);
                calculateInterestForAccount(account , interestRule , k, totalInterest);
                System.out.println("Interest rule: " + interestRule);
            });
            addInterestTransaction(account , totalInterest);
        });
    }

    public void calculateInterestForAccount(Account account , InterestRule interestRule, String rule, BigDecimal totalInterest) {
        Integer startDayofMonth= Integer.parseInt(rule.split("-")[0]);
        Integer endDayofMonth= Integer.parseInt(rule.split("-")[1]);
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),startDayofMonth);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),endDayofMonth);
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate , endDate , account.getAccountNumber());
        transactions.sort(Comparator.comparing(Transaction::getTransactionDate).reversed());
        BigDecimal balance = transactions.get(0).getBalance();
        Double interestRate = interestRule.getInterestRate();
        Double interestAmount = balance.doubleValue() * interestRate / 100 * endDayofMonth / 365;
        totalInterest.add(BigDecimal.valueOf(interestAmount));
    }

    public void addInterestTransaction(Account account , BigDecimal totalInterest) {
        Transaction transaction = new Transaction(LocalDate.now(),totalInterest, TransactionTypeEnum.INTEREST.code, account.getAccountNumber(),account.getBalance());
        transactionRepository.save(transaction);
    }
}
