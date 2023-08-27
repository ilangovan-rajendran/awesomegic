package com.awesomegic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction implements Serializable {
    private String id;
    private String transactionType;
    private BigDecimal transactionAmount;
    private LocalDate transactionDate;
    private String transactionId;
    private BigDecimal balance;
    private String accountNumber;

    public Transaction(LocalDate transactionDate, BigDecimal transactionAmount, String transactionType , String accountNumber, BigDecimal balance) {
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
