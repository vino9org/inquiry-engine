package net.vino9.vinobank.inqeng.api.dgs;

import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import graphql.relay.Connection;
import lombok.extern.slf4j.Slf4j;
import net.vino9.vinobank.inqeng.data.repository.CasaAccountRepository;
import net.vino9.vinobank.inqeng.data.repository.CasaTransactionRepository;
import net.vino9.vinobank.inqeng.geneated.types.CasaAccount;
import net.vino9.vinobank.inqeng.geneated.types.CasaTransaction;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@DgsComponent
@Slf4j
public class CasaAccountDataFetcher {

    final CasaAccountRepository casaAccountRepository;
    final CasaTransactionRepository casaTransactionRepository;
    final ModelMapper mapper;

    @Autowired
    public CasaAccountDataFetcher(CasaAccountRepository casaAccountRepository, CasaTransactionRepository casaTransactionRepository, ModelMapper mapper) {
        this.casaAccountRepository = casaAccountRepository;
        this.casaTransactionRepository = casaTransactionRepository;
        this.mapper = mapper;
    }

    @DgsQuery(field = "CasaAccount")
    public CasaAccount getCasaAccountDetail(@InputArgument String accountId) {
        log.info("query getCasaAccountDetail: {}", accountId);
        var account = casaAccountRepository.findByAccountId(accountId);
        if (account == null) {
            throw new DgsEntityNotFoundException();
        }
        return mapper.map(account, CasaAccount.class);
    }

    @DgsData(parentType = "CasaAccount", field = "transactions")
    public Connection<CasaTransaction> getTransactionsForAccount(
            DgsDataFetchingEnvironment dfe,
            @InputArgument Integer first,
            @InputArgument String after) {
        var accountId = ((CasaAccount) dfe.getSource()).getAccountId();
        log.info("query getTransactionsForCasaAccount for account {}", accountId);

        var startingPage = 0;
        if (after != null) {
            try {
                startingPage = Integer.parseInt(after);
            } catch (NumberFormatException e) {
                // do nothing, just start from 0
            }
        }

        var paging = PageRequest.of(startingPage, first);
        var page = casaTransactionRepository.findTransactionsByAccountId(accountId, paging);
        return ConnectionAssembler.fromPageable(page, mapper);
    }

    @DgsQuery(field = "CasaAccountsByCustomer")
    public List<CasaAccount> getCasaAccountsByCustomer(@InputArgument String customerId) {
        log.info("query getCasaAccountsByCustomer: {}", customerId);
        var accounts = casaAccountRepository.findByCustomerId(customerId);
        var type = new TypeToken<List<CasaAccount>>() {
        }.getType();
        return mapper.map(accounts, type);
    }
}