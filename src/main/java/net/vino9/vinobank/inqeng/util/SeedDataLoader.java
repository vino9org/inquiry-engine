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
    public void seed() {
        mongoTemplate.insert(CasaAccount.builder()
                .schemaVer("1")
                .accountId("123")
                .customerId("111")
                .currency("SGD")
                .balance("1000.00")
                .build(), "casa_accounts");

        mongoTemplate.insert(CasaTransaction.builder()
                .schemaVer("1")
                .accountId("123")
                .refId("10000001")
                .amount("100.00")
                .currency("SGD")
                .memo("transfer 1")
                .valueDate("2023-05-01")
                .build(), "casa_transactions");

        mongoTemplate.insert(CasaAccount.builder()
                .schemaVer("1")
                .accountId("124")
                .customerId("111")
                .currency("USD")
                .balance("888.88")
                .build(), "casa_accounts");

        mongoTemplate.insert(CasaTransaction.builder()
                .schemaVer("1")
                .accountId("124")
                .refId("10000002")
                .amount("12.00")
                .currency("USD")
                .memo("transfer 1")
                .valueDate("2023-04-01")
                .build(), "casa_transactions");

        log.info("seed data loaded");
    }
}
