package com.awesomegic.model;

import com.awesomegic.util.TransactionTypeEnum;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {
    @Id
    private String id;

    private String accountNumber;

    private BigDecimal balance;
    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void calculateBalance(BigDecimal balance , String transactionType) {
        BigDecimal currentBalance = BigDecimal.ZERO;
        if(TransactionTypeEnum.WITHDRAWAL.code.equalsIgnoreCase(transactionType)){
            currentBalance =  this.getBalance() != null ? this.getBalance().subtract(balance) : BigDecimal.ZERO ;
            if(currentBalance.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Insufficient balance");
                System.exit(0);
            }
        }else if(TransactionTypeEnum.DEPOSIT.code.equalsIgnoreCase(transactionType)) {
            currentBalance = this.getBalance() != null ?  this.getBalance().add(balance) : BigDecimal.ZERO;
        }
        this.balance = currentBalance;
}

    public static void main(String[] args) {
        String s = "20230626|AC001|W|100.00";
        System.out.println(s.split("\\|").length);
    }
}
