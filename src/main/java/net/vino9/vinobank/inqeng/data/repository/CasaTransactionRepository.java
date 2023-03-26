package net.vino9.vinobank.inqeng.data.repository;

import net.vino9.vinobank.inqeng.data.model.CasaTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CasaTransactionRepository extends MongoRepository<CasaTransaction, String> {
    @Query("{ 'accountId' : ?0 }")
    Page<CasaTransaction> findTransactionsByAccountId(String accountId, Pageable pageable);
}