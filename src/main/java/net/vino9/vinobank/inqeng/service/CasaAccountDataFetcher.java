package net.vino9.vinobank.inqeng.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
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


    @DgsQuery
    public CasaAccount getCasaAccountDetail(@InputArgument String accountId) {
        log.info("query getCasaAccountDetail: {}", accountId);
        return casaAccountRepository.findByAccountId(accountId);
    }

    @DgsQuery
    public List<CasaTransaction> getTransactionsForCasaAccount(@InputArgument String accountId) {
        log.info("query getTransactionsForCasaAccount for account {}", accountId);
        return casaTransactionRepository.findTransactionsByAccountId(accountId);
    }

    //getCasaAccountDetailWithTransactions
    @DgsQuery
    public CasaAccount getCasaAccountDetailWithTransactions(@InputArgument String accountId) {
        log.info("query getCasaAccountDetailWithTransactions for account {}", accountId);

        var account = casaAccountRepository.findByAccountId(accountId);
        if (account == null) {
            return null;
        }

        var transactions = casaTransactionRepository.findTransactionsByAccountId(accountId);
        account.setTransactions(transactions);

        return account;
    }
}