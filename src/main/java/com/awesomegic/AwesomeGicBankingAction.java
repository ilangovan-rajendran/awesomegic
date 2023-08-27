package com.awesomegic;


import com.awesomegic.model.Transaction;
import com.awesomegic.service.AccountService;
import com.awesomegic.service.InterestRuleService;
import com.awesomegic.service.TransactionService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


@NoArgsConstructor
@Component
public class AwesomeGicBankingAction {

    private AwesomegicValidationHelper awesomegicValidation;

    private AccountService accountService;

    private InterestRuleService interestRuleService;

    private TransactionService transactionService;



    @Autowired
    public AwesomeGicBankingAction(AccountService accountService, InterestRuleService interestRuleService, TransactionService transactionService, AwesomegicValidationHelper awesomegicValidation) {
        this.accountService = accountService;
        this.interestRuleService = interestRuleService;
        this.transactionService = transactionService;
        this.awesomegicValidation = awesomegicValidation;
    }

    public void processAction(String action) {
        Scanner scanner = new Scanner(System.in);
        String line = "";
        if(!awesomegicValidation.validateAction(action)) {
            System.out.println(action);
            System.out.println("Invalid action. Please try again.");
            return;
        }
        switch (action) {
            case "I":
                System.out.println("Please enter transaction details in <Date>|<Account>|<Type>|<Amount> format \n" +
                        "(or enter blank to go back to main menu):");

                line = scanner.nextLine();
                this.accountService.addAcount(line);
                break;
            case "D":
                System.out.println("Please enter interest rules details in <Date>|<RuleId>|<Rate in %> format \n" +
                        "(or enter blank to go back to main menu):\n" +
                        ">");
                line = scanner.nextLine();
                this.interestRuleService.addInterestRule(line);
                this.interestRuleService.printInterestRules();
                break;
            case "P":
                System.out.println("Please enter account and month to generate the statement <Account>|<Month>\n" +
                        "(or enter blank to go back to main menu):");
                line = scanner.nextLine();
                this.transactionService.printStatement(line);
                break;
            case "Q":
                System.out.println("Thank you for banking with AwesomeGIC Bank.\n" +
                        "Have a nice day!");
                break;
            default:
                System.out.println("Thank you for using AwesomeGIC Bank!");
                System.exit(0);
        }
    }


}
