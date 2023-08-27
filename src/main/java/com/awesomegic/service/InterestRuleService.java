package com.awesomegic.service;

import com.awesomegic.AwesomegicValidationHelper;
import com.awesomegic.model.InterestRule;
import com.awesomegic.repository.InterestRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InterestRuleService {

    private final InterestRuleRepository interestRuleRepository;

    private final AwesomegicValidationHelper awesomegicValidationHelper;

    @Autowired
    public InterestRuleService(InterestRuleRepository interestRuleRepository, AwesomegicValidationHelper awesomegicValidationHelper) {
        this.interestRuleRepository = interestRuleRepository;
        this.awesomegicValidationHelper = awesomegicValidationHelper;
    }


    public void addInterestRule(String line) {
        String[] interestRuleArray = line.split("\\|");
        //20230615|RULE03|2.20
        if(interestRuleArray == null || interestRuleArray.length != 3) {
            System.out.println("Invalid Interest Rule input. Please try again.");
            return;
        }
        String interestRuleDate = interestRuleArray[0];
        String interestRuleId = interestRuleArray[1];
        String interestRate = interestRuleArray[2];
        LocalDate interestRuleLocalDate = awesomegicValidationHelper.convertLocalDate(interestRuleDate);
        InterestRule interestRule = new InterestRule(interestRuleLocalDate,interestRuleId,interestRate);
        if(awesomegicValidationHelper.validateInterestRule(interestRule)){
            List<InterestRule> interestRules = interestRuleRepository.findByRuleDate(interestRuleLocalDate);
            if(!interestRules.isEmpty()){
                interestRule = interestRules.get(0);
                interestRule.setInterestRate(Double.parseDouble(interestRate));
            }
            interestRuleRepository.save(interestRule);
            System.out.println("-----Interest Rule Saved-----");
        }
    }

    public void printInterestRules() {
        List<InterestRule> interestRules = interestRuleRepository.findAllByOrderByRuleDateDesc();
        if(interestRules.isEmpty()) {
            System.out.println("No interest rules found.");
            return;
        }
        System.out.println("Date     | RuleId | Rate (%) |");
        interestRules.forEach(interestRule -> System.out.println(awesomegicValidationHelper.convertDateToString(interestRule.getRuleDate())+" | "+interestRule.getRuleId()+" | "+interestRule.getInterestRate()));
        System.out.println("-----End of Interest Rules-----");
    }
}
