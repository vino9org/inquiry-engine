package net.vino9.vinobank.inqeng.data.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "casa_transactions")
@Data
@Builder
public class CasaTransaction {
    private String id;
    private String schemaVer;
    private String refId;
    private String accountId;
    private String currency;
    private double amount;
    private LocalDate valueDate;
    private String memo;
}
