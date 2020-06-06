import com.google.common.collect.Sets;
import core.account.AccountingEntry;
import core.transaction.AccountingTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;

import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountingEntryTest {

    @Test
    public void testFreeze() {
        // Arrange
        AccountingEntry entry1 = new AccountingEntry(new BigDecimal(20), "0001", DEBIT);
        AccountingEntry entry2 = new AccountingEntry(new BigDecimal(20), "0002", CREDIT);
        AccountingTransaction transaction = new AccountingTransaction(Sets.newHashSet(entry1, entry2));
        // Act
        Executable act = () -> entry1.setTransaction(transaction);
        // Assert
        assertThrows(IllegalStateException.class, act);
    }
}
