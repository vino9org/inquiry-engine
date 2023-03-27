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

    public static final String QUERY_FIELDS = """
                        accountId
                        balance
                        currency
                        transactions {
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
            """;
    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void test_get_account_details_with_pagination() {
        var queryTemplate = """ 
                    {
                        CasaAccount(accountId: "123") {
                        %s
                        }
                    }
                """;

        // first page
        var result = dgsQueryExecutor.executeAndGetDocumentContext(String.format(queryTemplate, QUERY_FIELDS));

        String accountId = result.read("data.CasaAccount.accountId");
        assertThat(accountId).isEqualTo("123");

        List<String> refIds = result.read("data.CasaAccount.transactions.edges[*].node.refId");
        assertThat(refIds).hasSize(10);

        Map<String, Object> pageInfo = result.read("data.CasaAccount.transactions.pageInfo");
        assertThat((boolean) pageInfo.get("hasPreviousPage")).isFalse();
        assertThat((boolean) pageInfo.get("hasNextPage")).isTrue();

        // second page, has full page of data, 10 elements
        var afterPage = (String) pageInfo.get("endCursor");
        result = dgsQueryExecutor.executeAndGetDocumentContext(String.format(queryTemplate, fieldsForPage(10, 1)));

        refIds = result.read("data.CasaAccount.transactions.edges[*].node.refId");
        assertThat(refIds).hasSize(10);

        pageInfo = result.read("data.CasaAccount.transactions.pageInfo");
        assertThat((boolean) pageInfo.get("hasPreviousPage")).isTrue();
        assertThat((boolean) pageInfo.get("hasNextPage")).isTrue();

        // last page, only has 8 elements
        afterPage = (String) pageInfo.get("endCursor");
        result = dgsQueryExecutor.executeAndGetDocumentContext(String.format(queryTemplate, fieldsForPage(10, 2)));

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
                """, "123", QUERY_FIELDS);

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
                """, "111", QUERY_FIELDS);

        var result = dgsQueryExecutor.executeAndGetDocumentContext(query);

        List<String> currencies = result.read("data.CasaAccountsByCustomer[*].currency");
        assertThat(currencies).contains("SGD").contains("USD");

        List<String> refIds = result.read("data.CasaAccountsByCustomer[*].transactions.edges[*].node.refId");
        assertThat(refIds).contains("10000001").contains("10000002");
    }

    @Test
    void test_non_existent_account() {
        var query = " { CasaAccount(accountId:\"bad_number\") { accountId } }";
        var thrown = assertThrows(QueryException.class, () -> {
            dgsQueryExecutor.executeAndGetDocumentContext(query);
        });
        assertThat(thrown.getErrors().get(0).getMessage()).contains("Requested entity not found");
    }

    @Test
    void test_non_existent_customer() {
        var query = " { CasaAccountsByCustomer(customerId: \"007\") { accountId } }";
        List<String> result = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.CasaAccountsByCustomer[*].accountId");
        assertThat(result).isEmpty();
    }

    private String fieldsForPage(int pageSize, int pageNumber) {
        var target = String.format("transactions (first: %d, after: \"%d\") {", pageSize, pageNumber);
        return QUERY_FIELDS.replace("transactions {", target);
    }
}
