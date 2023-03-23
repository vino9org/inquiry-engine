package net.vino9.vinobank.inqeng.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "casa_transactions")
@Data
public class CasaTransaction {
    private String id;
    private String schemaVer;
    private String refId;
    private String accountId;
    private String currency;
    private String amount;
    private String valueDate;
    private String memo;
}
