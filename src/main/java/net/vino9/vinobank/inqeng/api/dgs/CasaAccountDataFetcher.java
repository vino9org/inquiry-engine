package net.vino9.vinobank.inqeng.api.dgs;

import com.netflix.graphql.dgs.*;
import graphql.relay.Connection;
import graphql.relay.SimpleListConnection;
import lombok.extern.slf4j.Slf4j;
import net.vino9.vinobank.inqeng.api.types.CasaAccount;
import net.vino9.vinobank.inqeng.api.types.CasaTransaction;
import net.vino9.vinobank.inqeng.data.repository.CasaAccountRepository;
import net.vino9.vinobank.inqeng.data.repository.CasaTransactionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
@Slf4j
public class CasaAccountDataFetcher {

    final CasaAccountRepository casaAccountRepository;
    final CasaTransactionRepository casaTransactionRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public CasaAccountDataFetcher(CasaAccountRepository casaAccountRepository, CasaTransactionRepository casaTransactionRepository) {
        this.casaAccountRepository = casaAccountRepository;
        this.casaTransactionRepository = casaTransactionRepository;
    }


    @DgsQuery(field = "CasaAccount")
    public CasaAccount getCasaAccountDetail(@InputArgument String accountId) {
        log.info("query getCasaAccountDetail: {}", accountId);
        var account = casaAccountRepository.findByAccountId(accountId);
        if (account == null) {
            return null;
        }
        return mapper.map(account, CasaAccount.class);
    }

    @DgsData(parentType = "CasaAccount", field = "transactions")
    public Connection<CasaTransaction> getTransactionsForAccount(DgsDataFetchingEnvironment dfe) {
        var accountId = ((CasaAccount) dfe.getSource()).getAccountId();
        log.info("query getTransactionsForCasaAccount for account {}", accountId);
        var transactions = casaTransactionRepository.findTransactionsByAccountId(accountId);
        List<CasaTransaction> output = mapper.map(transactions, new TypeToken<List<CasaTransaction>>() {
        }.getType());
        return new SimpleListConnection<CasaTransaction>(output).get(dfe);
    }

    @DgsQuery(field = "CasaAccountsByCustomer")
    public List<CasaAccount> getCasaAccountsByCustomer(@InputArgument String customerId) {
        log.info("query getCasaAccountsByCustomer: {}", customerId);
        var accounts = casaAccountRepository.findByCustomerId(customerId);
        return mapper.map(accounts, new TypeToken<List<CasaAccount>>() {}.getType());
    }


}