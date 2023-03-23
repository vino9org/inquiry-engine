package net.vino9.vinobank.inqeng.repository;

import net.vino9.vinobank.inqeng.model.CasaTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasaTransactionRepository extends MongoRepository<CasaTransaction, String> {
    @Query("{ 'accountId' : ?0 }")
    List<CasaTransaction> findTransactionsByAccountId(String accountId);
}