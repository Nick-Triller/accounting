import core.chartofaccounts.ChartOfAccounts;
import core.chartofaccounts.ChartOfAccountsBuilder;
import org.junit.jupiter.api.Test;

import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChartOfAccountsTest {
    @Test
    public void testCreateCoa() {
        // Arrange
        String cashAccountNumber = "000001";
        String checkingAccountNumber = "000002";
        String liabilitiesAccountNumber = "000003";

        // Act
        ChartOfAccounts coa = ChartOfAccountsBuilder.create()
                .addAccount(cashAccountNumber, "Cash", DEBIT)
                .addAccount(checkingAccountNumber, "Checking", DEBIT)
                .addAccount(liabilitiesAccountNumber, "Liabilities", CREDIT)
                .build();

        // Assert
        assertEquals(3, coa.getAccountNumberToAccountDetails().size());
    }
}
