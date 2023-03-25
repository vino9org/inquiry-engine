package net.vino9.vinobank.inqeng.api.types;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
