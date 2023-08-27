package com.awesomegic.repository;

import com.awesomegic.model.InterestRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterestRuleRepository extends MongoRepository<InterestRule, String>{

    @Query("{ 'ruleDate' : ?0}")
    List<InterestRule> findByRuleDate(LocalDate ruleDate);

    @Query("{ 'ruleId' : ?0}")
    List<InterestRule> findByRuleId(String ruleId);


    List<InterestRule> findAllByOrderByRuleDateDesc();
}
