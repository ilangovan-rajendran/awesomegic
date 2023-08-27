package com.awesomegic.repository;

import com.awesomegic.model.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    @Query("{ 'transactionDate' : ?0 , 'accountNumber': ?1}")
    List<Transaction> findByTransactionDateOrderByTransactionDateDesc(LocalDate date, String accountNo , Sort sort);
    @Query("{'transactionDate': {$gte: ?0, $lt: ?1} , 'accountNumber': ?2}")
    List<Transaction> findByTransactionDateBetween(LocalDate startDayMonth , LocalDate lastDayMonth , String accountNo);
}
