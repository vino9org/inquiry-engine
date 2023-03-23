package net.vino9.vinobank.inqeng.repository;

import net.vino9.vinobank.inqeng.model.CasaAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasaAccountRepository extends MongoRepository<CasaAccount, String> {
    @Query("{ 'accountId' : ?0 }")
    List<CasaAccount> findByAccountId(String accountId);
}
