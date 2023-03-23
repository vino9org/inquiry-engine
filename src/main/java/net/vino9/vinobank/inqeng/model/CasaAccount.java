package net.vino9.vinobank.inqeng.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "casa_accounts")
@Data
@Builder
public class CasaAccount {
    @Id
    private String id;
    private String schemaVer;
    private String accountId;
    private String currency;
    private String balance;
    private CasaTransaction[] transactions;
}
