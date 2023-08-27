package com.awesomegic.repository;

import com.awesomegic.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends  MongoRepository<Account, String> {

    public Account findByAccountNumber(String accountNumber);
}
