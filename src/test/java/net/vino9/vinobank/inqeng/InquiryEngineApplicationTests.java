package net.vino9.vinobank.inqeng;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
class InquiryEngineApplicationTests {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void test_get_account_details_with_pagination() {
        var queryTemplate = "{\n" +
                            "    CasaAccount(accountId: \"123\") {\n" +
                            "    %s\n" +
                            "    }\n" +
                            "}\n";

        var result = dgsQueryExecutor.executeAndGetDocumentContext(String.format(queryTemplate, queryFields(10)));

        String accountId = result.read("data.CasaAccount.accountId");
        assertThat(accountId).isEqualTo("123");

        List<String> refIds = result.read("data.CasaAccount.transactions.edges[*].node.refId");
        assertThat(refIds).hasSize(10);

        Map<String, Object> pageInfo = result.read("data.CasaAccount.transactions.pageInfo");
        assertThat((boolean) pageInfo.get("hasPreviousPage")).isFalse();
        assertThat((boolean) pageInfo.get("hasNextPage")).isTrue();

        var afterPage = (String) pageInfo.get("endCursor");
        result = dgsQueryExecutor.executeAndGetDocumentContext(String.format(queryTemplate, queryFields(10, afterPage)));

        refIds = result.read("data.CasaAccount.transactions.edges[*].node.refId");
        assertThat(refIds).hasSize(10);

        pageInfo = result.read("data.CasaAccount.transactions.pageInfo");
        assertThat((boolean) pageInfo.get("hasPreviousPage")).isTrue();
        assertThat((boolean) pageInfo.get("hasNextPage")).isTrue();

        afterPage = (String) pageInfo.get("endCursor");
        result = dgsQueryExecutor.executeAndGetDocumentContext(String.format(queryTemplate, queryFields(10, afterPage)));

        refIds = result.read("data.CasaAccount.transactions.edges[*].node.refId");
        assertThat(refIds).hasSize(8);

        pageInfo = result.read("data.CasaAccount.transactions.pageInfo");
        assertThat((boolean) pageInfo.get("hasPreviousPage")).isTrue();
        assertThat((boolean) pageInfo.get("hasNextPage")).isFalse();
        assertThat((String) pageInfo.get("endCursor")).isEqualTo(afterPage);

    }

    @Test
    void test_get_account_details() {
        var query = String.format("""
                {
                    CasaAccount(accountId: "%s") {
                    %s
                    }
                }
                """, "123", queryFields(10));

        // page 1
        var result = dgsQueryExecutor.executeAndGetDocumentContext(query);

        String accountId = result.read("data.CasaAccount.accountId");
        assertThat(accountId).isEqualTo("123");

        List<String> refIds = result.read("data.CasaAccount.transactions.edges[*].node.refId");
        assertThat(refIds).contains("10000001");
    }


    @Test
    void test_get_accounts_by_customer_id() {
        var query = String.format("""
                {
                    CasaAccountsByCustomer(customerId: "%s") {
                    %s
                    }
                }
                """, "111", queryFields(10));

        var result = dgsQueryExecutor.executeAndGetDocumentContext(query);

        List<String> currencies = result.read("data.CasaAccountsByCustomer[*].currency");
        assertThat(currencies).contains("SGD").contains("USD");

        List<String> refIds = result.read("data.CasaAccountsByCustomer[*].transactions.edges[*].node.refId");
        assertThat(refIds).contains("10000001").contains("10000002");
    }

    @Test
    void test_non_existent_account() {
        var query = String.format(" { CasaAccount(accountId:\"%s\") { accountId transactions { edges { node { refId } } } } }", "bad_bad");
        assertThrows(QueryException.class, () -> {
            dgsQueryExecutor.executeAndGetDocumentContext(query);
        });
    }

    @Test
    void test_non_existent_customer() {
        var query = String.format(" { CasaAccountsByCustomer(customerId: \"%s\") { accountId currency transactions { edges { node { refId } } } } }", "007");
        List<String> result = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.CasaAccountsByCustomer[*].accountId");
        assertThat(result).isEmpty();
    }

    private String queryFields(int first, String after) {
        var fields = queryFields(first);
        var source = String.format("(first: %d)", first);
        var target = String.format("(first: %d, after: \"%s\")", first, after);
        return fields.replace(source, target);
    }

    private String queryFields(int first) {
        return String.format("""
                            accountId
                            balance
                            currency
                            transactions(first: %d) {
                                edges {
                                    node {
                                        refId
                                        amount
                                    }
                                }
                                pageInfo {
                                    startCursor
                                    endCursor
                                    hasPreviousPage
                                    hasNextPage
                                }
                            }
                """, first);
    }
}
