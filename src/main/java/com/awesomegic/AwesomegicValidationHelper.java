package com.awesomegic;

import com.awesomegic.model.Account;
import com.awesomegic.model.InterestRule;
import com.awesomegic.model.Transaction;
import com.awesomegic.service.AbstractService;
import com.awesomegic.util.ActionEnum;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@NoArgsConstructor
public class AwesomegicValidationHelper extends AbstractService {

    public boolean validateAccountNumber(String accountNumber) {
        return accountNumber.matches("^[a-zA-Z0-9]+$");
    }

    public boolean validateAmount(String amount) {
        return amount.matches("^\\d+(\\.\\d{1,2})?$");
    }

    public boolean validateInterestRate(String interestRate) {
        return  interestRate.matches("^\\d+(\\.\\d{1,2})?$");
    }

    public boolean validateAction(String action) {
        return ActionEnum.getActionEnum(action) != null;
    }

    public static void main(String[] args) {
        AwesomegicValidationHelper awesomegicValidation = new AwesomegicValidationHelper();
        System.out.println(awesomegicValidation.validateAccountNumber("AC001"));
        System.out.println(awesomegicValidation.validateAmount("100"));
        System.out.println(awesomegicValidation.validateAction("F"));
    }

    public  LocalDate convertLocalDate(String transactionDate) {
        try{
            return LocalDate.parse(transactionDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        }catch (Exception e) {
            super.printInvalidMessage("Invalid date. Please try again.");
            System.exit(0);
        }
        return null;
    }

    public String convertDateToString(LocalDate transactionDate) {
        return transactionDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public boolean validateTransaction(Transaction transaction) {
        return  validateAmount(transaction.getTransactionAmount().toString());
    }

    public boolean validateMonth(String month) {
        return month.matches("^(0?[1-9]|1[012])$");
    }

    public String generateTransactionId(String transactionId) {
        if(transactionId == null || transactionId.isEmpty()) {
            String tranDate = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYMMdd"));
            return tranDate+"-" + "01";
        }
        String date = transactionId.substring(0,8);
        Integer count = Integer.parseInt(transactionId.substring(9,11));
        count++;
        return date +"-"+ String.format("%02d", count);
    }


    public boolean validateInterestRule(InterestRule interestRule) {
        if(interestRule.getInterestRate() != null && Double.compare(interestRule.getInterestRate() , Double.valueOf(0.0)) > 0
                && Double.compare(interestRule.getInterestRate() , Double.valueOf(100.0)) < 0) {
            return true;
        }else{
            super.printInvalidMessage("Invalid interest rate. Please try again.");
            return false;
        }
    }
}
