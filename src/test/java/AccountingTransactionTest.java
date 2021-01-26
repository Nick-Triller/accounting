import core.account.AccountSide;
import core.account.AccountingEntry;
import core.transaction.AccountingTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountingTransactionTest {

    @Test
    public void testBalanced() {
        // Arrange
        Set<AccountingEntry> entries = new HashSet<>();
        entries.add(new AccountingEntry(new BigDecimal(50), "Cash", AccountSide.DEBIT));
        entries.add(new AccountingEntry(new BigDecimal(50), "Liabilities", AccountSide.CREDIT));
        // Act + Assert
        AccountingTransaction t = new AccountingTransaction(entries);
    }

    @Test
    public void testUnbalanced() {
        // Arrange
        Set<AccountingEntry> entries = new HashSet<>();
        entries.add(new AccountingEntry(new BigDecimal(10), "Cash", AccountSide.DEBIT));
        entries.add(new AccountingEntry(new BigDecimal(50), "Liabilities", AccountSide.CREDIT));
        // Act
        Executable act = () -> new AccountingTransaction(entries);
        // Assert
        assertThrows(IllegalArgumentException.class, act);
    }

    @Test
    public void testInfo() {
        // Arrange
        Set<AccountingEntry> entries = new HashSet<>();
        entries.add(new AccountingEntry(new BigDecimal(50), "Cash", AccountSide.DEBIT));
        entries.add(new AccountingEntry(new BigDecimal(50), "Liabilities", AccountSide.CREDIT));
        // Act
        Map<String, String> info = new HashMap<>();
        info.put("creditor", "ACME inc.");
        var transaction = new AccountingTransaction(entries, info);
        // Assert
        assertEquals(transaction.getInfo(), info);
    }
}
