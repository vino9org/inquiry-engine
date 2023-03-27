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
            throw new DgsEntityNotFoundException();
        }
        return mapper.map(account, CasaAccount.class);
    }

    @DgsData(parentType = "CasaAccount", field = "transactions")
    public Connection<CasaTransaction> getTransactionsForAccount(
            DgsDataFetchingEnvironment dfe,
            @InputArgument int first,
            @InputArgument String after) {
        var accountId = ((CasaAccount) dfe.getSource()).getAccountId();
        log.info("query getTransactionsForCasaAccount for account {}", accountId);

        var startingPage = 0;
        if (after != null) {
            try {
                startingPage = Integer.valueOf(after).intValue();
            } catch (NumberFormatException e) {
                // do nothing, just start from 0
            }
        }

        var paging = PageRequest.of(startingPage, first);
        var page = casaTransactionRepository.findTransactionsByAccountId(accountId, paging);
        var type = new TypeToken<List<CasaTransaction>>() {
        }.getType();
        List<CasaTransaction> output = mapper.map(page.getContent(), type);
        return ConnectionAssembler.convert(output, page.getNumber(), page.getTotalPages());
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