package net.vino9.vinobank.inqeng.util;

import lombok.extern.java.Log;
import net.vino9.vinobank.inqeng.model.CasaAccount;
import net.vino9.vinobank.inqeng.model.CasaTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Log
public class SeedDataLoader {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SeedDataLoader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void seed() throws Exception {
        mongoTemplate.insert(CasaAccount.builder()
                .schemaVer("1")
                .accountId("123")
                .currency("SGD")
                .balance("1000.00")
                .build(), "casa_accounts");

        mongoTemplate.insert(CasaTransaction.builder()
                .schemaVer("1")
                .accountId("123")
                .refId("10000001")
                .amount("100.00")
                .memo("transfer 1")
                .build(), "casa_transactions");

        log.info("seed data loaded");
    }
}
