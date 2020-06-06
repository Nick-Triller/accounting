import core.account.AccountingEntry;
import core.transaction.AccountingTransaction;
import core.transaction.AccountingTransactionBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AccountingTransactionBuilderTest {
    @Test
    public void testValidTransaction() {
        // Arrange
        String cashAccountNumber = "01";
        String checkingAccountNumber = "02";
        String liabilitiesAccountNumber = "11";

        // Act
        // We borrow money and get it part of it in cash, the rest as a wire transfer
        AccountingTransaction t = AccountingTransactionBuilder
                .create(null)
                .debit(new BigDecimal(10), cashAccountNumber)
                .debit(new BigDecimal(25), checkingAccountNumber)
                .credit(new BigDecimal(35), liabilitiesAccountNumber)
                .build();

        // Assert
        assertEquals(new BigDecimal(10), getEntryById(t, cashAccountNumber).getAmount());
        assertEquals(new BigDecimal(25), getEntryById(t, checkingAccountNumber).getAmount());
        assertEquals(new BigDecimal(35), getEntryById(t, liabilitiesAccountNumber).getAmount());
    }

    private AccountingEntry getEntryById(AccountingTransaction t, String accountId) {
        return t.getEntries().stream()
                .filter(e -> Objects.equals(e.getAccountNumber(), accountId))
                .findFirst().get();
    }
}
