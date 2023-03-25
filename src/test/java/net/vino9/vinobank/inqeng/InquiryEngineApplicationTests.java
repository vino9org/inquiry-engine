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
        var query = String.format(" { CasaAccount(accountId:\"%s\") { accountId transactions { edges { node { refId } } } } }", "123");
        var result = dgsQueryExecutor.executeAndGetDocumentContext(query);

        String accountId = result.read("data.CasaAccount.accountId");
        assertThat(accountId).isEqualTo("123");

        List<String> refIds = result.read("data.CasaAccount.transactions.edges[*].node.refId");
        assertThat(refIds).contains("10000001");
    }

    @Test
    void test_get_accounts_by_customer_id() {
        var query = String.format(" { CasaAccountsByCustomer(customerId: \"%s\") { accountId currency transactions { edges { node { refId } } } } }", "111");
        var result = dgsQueryExecutor.executeAndGetDocumentContext(query);

        List<String> currencies = result.read("data.CasaAccountsByCustomer[*].currency");
        assertThat(currencies).contains("SGD").contains("USD");

        List<String> refIds = result.read("data.CasaAccountsByCustomer[*].transactions.edges[*].node.refId");
        assertThat(refIds).contains("10000001").contains("10000002");
    }

    @Test
    void test_non_existent_account() {
        var query = String.format(" { CasaAccount(accountId:\"%s\") { accountId transactions { edges { node { refId } } } } }", "bad_bad");
        var result = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.CasaAccount");
        assertThat(result).isNull();
    }

    @Test
    void test_non_existent_customer() {
        var query = String.format(" { CasaAccountsByCustomer(customerId: \"%s\") { accountId currency transactions { edges { node { refId } } } } }", "007");
        List<String> result = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.CasaAccountsByCustomer[*].accountId");
        assertThat(result).isEmpty();
    }

}
