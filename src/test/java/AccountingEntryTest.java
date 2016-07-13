import com.google.common.collect.Sets;
import core.account.AccountingEntry;
import core.transaction.AccountingTransaction;
import org.junit.Test;

import java.math.BigDecimal;

import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;

public class AccountingEntryTest {

    @Test(expected = IllegalStateException.class)
    public void testFreeze() {
        // Arrange
        AccountingEntry entry1 = new AccountingEntry(new BigDecimal(20), "0001", DEBIT);
        AccountingEntry entry2 = new AccountingEntry(new BigDecimal(20), "0002", CREDIT);
        AccountingTransaction transaction = new AccountingTransaction(Sets.newHashSet(entry1, entry2));
        // Act + Assert
        entry1.setTransaction(transaction);
    }
}
