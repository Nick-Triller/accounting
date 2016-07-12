import com.google.common.collect.Sets;
import core.account.AccountSide;
import core.account.AccountingEntry;
import core.transaction.AccountingTransaction;
import core.transaction.AccountingTransactionBuilder;
import org.junit.Test;

import java.math.BigDecimal;

import static core.account.AccountSide.*;

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
