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
    void test_get_account_details_single_without_transactions() {
        List<String> accounts = dgsQueryExecutor.executeAndExtractJsonPath(
                " { getCasaAccountDetail(accountId:\"123\") { accountId } }",
                "data.getCasaAccountDetail[*].accountId");

        assertThat(accounts).contains("123");
    }
}
