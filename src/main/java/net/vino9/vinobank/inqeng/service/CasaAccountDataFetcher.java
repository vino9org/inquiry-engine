package net.vino9.vinobank.inqeng.service;

import com.netflix.graphql.dgs.*;
import lombok.extern.slf4j.Slf4j;
import net.vino9.vinobank.inqeng.model.CasaAccount;
import net.vino9.vinobank.inqeng.model.CasaTransaction;
import net.vino9.vinobank.inqeng.repository.CasaAccountRepository;
import net.vino9.vinobank.inqeng.repository.CasaTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
@Slf4j
public class CasaAccountDataFetcher {

    final CasaAccountRepository casaAccountRepository;
    final CasaTransactionRepository casaTransactionRepository;

    @Autowired
    public CasaAccountDataFetcher(CasaAccountRepository casaAccountRepository, CasaTransactionRepository casaTransactionRepository) {
        this.casaAccountRepository = casaAccountRepository;
        this.casaTransactionRepository = casaTransactionRepository;
    }


    @DgsQuery(field = "CasaAccount")
    public CasaAccount getCasaAccountDetail(@InputArgument String accountId) {
        log.info("query getCasaAccountDetail: {}", accountId);
        return casaAccountRepository.findByAccountId(accountId);
    }

    @DgsData(parentType = "CasaAccount", field = "transactions")
    public List<CasaTransaction> getTransactionsForAccount(DgsDataFetchingEnvironment dfe) {
        var accountId = ((CasaAccount) dfe.getSource()).getAccountId();
        log.info("query getTransactionsForCasaAccount for account {}", accountId);
        return casaTransactionRepository.findTransactionsByAccountId(accountId);
    }

    @DgsQuery(field = "CasaAccountsByCustomer")
    public List<CasaAccount> getCasaAccountsByCustomer(@InputArgument String customerId) {
        log.info("query getCasaAccountsByCustomer: {}", customerId);
        return casaAccountRepository.findByCustomerId(customerId);
    }


}