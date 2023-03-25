package net.vino9.vinobank.inqeng.api.types;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
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
