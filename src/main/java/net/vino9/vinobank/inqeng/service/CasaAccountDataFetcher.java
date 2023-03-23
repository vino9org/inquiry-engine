package net.vino9.vinobank.inqeng.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.extern.slf4j.Slf4j;
import net.vino9.vinobank.inqeng.model.CasaAccount;
import net.vino9.vinobank.inqeng.repository.CasaAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
@Slf4j
public class CasaAccountDataFetcher {

    final
    CasaAccountRepository casaAccountRepository;

    @Autowired
    public CasaAccountDataFetcher(CasaAccountRepository casaAccountRepository) {
        this.casaAccountRepository = casaAccountRepository;
    }

    @DgsQuery
    public List<CasaAccount> getCasaAccountDetail(@InputArgument String accountId) {
        log.info("query getCasaAccountDetail: {}", accountId);
        return casaAccountRepository.findByAccountId(accountId);
    }
}
