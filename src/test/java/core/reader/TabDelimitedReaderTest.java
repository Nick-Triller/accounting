package core.reader;

import core.Journal;
import core.Ledger;
import core.account.AccountDetails;
import core.account.AccountDetailsImpl;
import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;
import core.account.AccountingEntry;
import core.chartofaccounts.ChartOfAccounts;
import core.transaction.AccountingTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class TabDelimitedReaderTest {
    @Test
    void testReadAccounts() {
        // Arrange
        Reader x = new TabDelimitedReader("./src/test/resources/test-accounts.tsv");
        Ledger y = x.read();
        // Act
        ChartOfAccounts coa = y.getCoa();
        // Assert
        assertNotNull(coa);
        Map<String, AccountDetails> ys = coa.getAccountNumberToAccountDetails();
        Set<String> expectedAccountIds = Set.of(
                "AST_0001",
                "EXP_0001",
                "LIA_0001",
                "EQU_0001",
                "REV_0001"
        );
        assertEquals(expectedAccountIds, ys.keySet());
        assertEquals(
                new AccountDetailsImpl("AST_0001", "checking", DEBIT),
                ys.get("AST_0001"));
        assertEquals(
                new AccountDetailsImpl("EXP_0001", "income", DEBIT),
                ys.get("EXP_0001"));
        assertEquals(
                new AccountDetailsImpl("LIA_0001", "expenses", CREDIT),
                ys.get("LIA_0001"));
        assertEquals(
                new AccountDetailsImpl("EQU_0001", "equity", CREDIT),
                ys.get("EQU_0001"));
        assertEquals(
                new AccountDetailsImpl("REV_0001", "revenue", CREDIT),
                ys.get("REV_0001"));
    }

    @Test
    void testReadTransaction() {
        // Arrange
        Reader x = new TabDelimitedReader("./src/test/resources/test-transactions.tsv");
        // Act
        Ledger ledger = x.read();
        // Assert
        Journal journal = ledger.getJournal();
        List<AccountingTransaction> transactions = journal.getTransactions();
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        AccountingTransaction t0 = transactions.get(0);
        assertEquals(1322925330000L, t0.getBookingDateTimestamp());
        Map<String, AccountingEntry> numberToEntry = t0
                .getEntries()
                .stream()
                .collect(Collectors.toMap(AccountingEntry::getAccountNumber, entry -> entry));
        assertEquals(3, numberToEntry.size());
        AccountingEntry e = numberToEntry.get("A");
        assertEquals(CREDIT, e.getAccountSide());
        assertEquals(new BigDecimal("300.25"), e.getAmount());
        e = numberToEntry.get("X");
        assertEquals(DEBIT, e.getAccountSide());
        assertEquals(new BigDecimal("200.15"), e.getAmount());
        e = numberToEntry.get("L");
        assertEquals(DEBIT, e.getAccountSide());
        assertEquals(new BigDecimal("100.10"), e.getAmount());
    }
}
