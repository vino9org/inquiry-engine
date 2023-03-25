package net.vino9.vinobank.inqeng.data.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "casa_accounts")
@Data
@Builder
public class CasaAccount {
    @Id
    private String id;
    private String schemaVer;
    private String accountId;
    private String customerId;
    private String currency;
    private double balance;
    private List<CasaTransaction> transactions;
}
