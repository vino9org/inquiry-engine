package net.vino9.vinobank.inqeng.data.repository;

import net.vino9.vinobank.inqeng.data.model.CasaAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasaAccountRepository extends MongoRepository<CasaAccount, String> {
    @Query("{ 'accountId' : ?0 }")
    CasaAccount findByAccountId(String accountId);

    @Query("{ 'customerId' : ?0 }")
    List<CasaAccount> findByCustomerId(String customerId);
}
