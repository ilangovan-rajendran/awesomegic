package com.awesomegic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "interestrules")
public class InterestRule {

    @Id
    private String id;
    private String ruleId;
    private LocalDate ruleDate;
    private Double interestRate;

    public InterestRule(LocalDate interestRuleDate, String interestRuleId, String interestRate) {
        this.ruleDate = interestRuleDate;
        this.ruleId = interestRuleId;
        this.interestRate = Double.parseDouble(interestRate);
    }
}
