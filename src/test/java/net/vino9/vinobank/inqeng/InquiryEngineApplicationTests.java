package net.vino9.vinobank.inqeng;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class InquiryEngineApplicationTests {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void test_get_account_details() {
        var id = "123";
        var outputId = dgsQueryExecutor.executeAndExtractJsonPath(
                String.format(" { getCasaAccountDetail(accountId:\"%s\") { accountId } }", id),
                "data.getCasaAccountDetail.accountId");

        assertThat(outputId).isEqualTo(id);
    }

    @Test
    void test_get_transactions_for_casa_account() {
        var id = "123";
        List<String> transactions = dgsQueryExecutor.executeAndExtractJsonPath(
                String.format(" { getTransactionsForCasaAccount(accountId:\"%s\") { refId } }", id),
                "data.getTransactionsForCasaAccount[*].refId");

        assertThat(transactions).contains("10000001");
    }

    @Test
    void test_get_casa_account_detail_with_transactions() {
        var id = "123";
        List<String> transactions = dgsQueryExecutor.executeAndExtractJsonPath(
                String.format(" { getCasaAccountDetailWithTransactions(accountId:\"%s\") { accountId transactions { refId }} }", id),
                "data.getCasaAccountDetailWithTransactions.transactions[*].refId");

        assertThat(transactions).contains("10000001");
    }
}
