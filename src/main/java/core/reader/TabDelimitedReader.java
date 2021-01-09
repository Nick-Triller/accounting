package core.reader;

import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import core.Journal;
import core.Ledger;
import core.account.AccountDetails;
import core.account.AccountDetailsImpl;
import core.account.AccountSide;
import core.account.AccountingEntry;
import core.chartofaccounts.ChartOfAccounts;
import core.transaction.AccountingTransaction;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Value;

/**
 A TabDelimitedReader reads the chart of accounts and transactions
 from a tab-delimited file.

 <p>
 See sample files in test/resources for examples.
 Skips blanks lines and lines that start with a '#'.
 </p>

 */
@Value
public class TabDelimitedReader implements Reader {
    public static final String DELIMITER = "\\t";
    String fileName;

    @Override
    public Ledger read() {
        Set<AccountDetails> accountDetails = new HashSet<>();
        List<AccountingTransaction> transactions = new ArrayList<>();
        try (Stream<String> rows = Files.lines(Path.of(fileName))) {
            rows
                    .map(row -> row.split(DELIMITER))
                    .forEach(x -> processRow(x, accountDetails, transactions));
        } catch (IOException e) {
            throw new IllegalStateException("can't read '" + fileName + "'", e);
        }
        Journal journal = new Journal();
        transactions.forEach(journal::addTransaction);
        return new Ledger(
                journal,
                new ChartOfAccounts(Set.copyOf(accountDetails))
        );
    }

    // Convert row to either an AccountDetail or AccountingTransaction.
    private void processRow(
            String[] columns,
            Set<AccountDetails> accountDetails,
            List<AccountingTransaction> transactions) {
        // Skip blank lines.
        if (columns.length == 0 || columns[0].isBlank()) {
            return;
        }
        // Skip lines that are comments.
        if (columns[0].trim().startsWith("#")) {
            return;
        }
        // Skip header line.
        if (columns[0].trim().equalsIgnoreCase("entry_type")) {
            return;
        }
        EventType eventType = null;
        try {
            eventType = EventType.valueOf(columns[0]);
        } catch (Exception e) {
            validationErr("invalid event type", e, columns);
        }
        switch (eventType) {
            case ACCOUNT:
                accountDetails.add(rowToAccount(columns));
                break;
            case TRANSACTION:
                transactions.add(rowToTransaction(columns));
                break;
            default:
                logicErr(columns);
        }
    }

    private AccountDetails rowToAccount(String[] columns) {
        String number = columns[1].trim();
        String name = columns[2].trim();
        String increasesOn = columns[3].trim();
        AccountDetails y = null;
        try {
            y = new AccountDetailsImpl(
                    number,
                    name,
                    AccountSide.valueOf(increasesOn));
        } catch (Exception e) {
            validationErr("invalid account data", e, columns);
        }
        return y;
    }

    private AccountingTransaction rowToTransaction(String[] columns) {
        String dateTime = columns[4].trim();
        String debitAccounts = columns[5].trim();
        String debitAmounts = columns[6].trim();
        String creditAccounts = columns[7].trim();
        String creditAmounts = columns[8].trim();
        Set<AccountingEntry> entries = toAccountingEntryset(
                DEBIT,
                debitAccounts,
                debitAmounts);
        entries.addAll(toAccountingEntryset(
                CREDIT,
                creditAccounts,
                creditAmounts));
        // 2011-12-03T10:15:30+01:00
        long transactionTime = ZonedDateTime
                .parse(dateTime, ISO_OFFSET_DATE_TIME)
                .toInstant()
                .toEpochMilli();
        AccountingTransaction y = null;
        try {
            y = new AccountingTransaction(
                    entries,
                    null,
                    transactionTime);
        } catch (Exception e) {
            validationErr("invalid transaction data", e, columns);
        }
        return y;
    }

    private Set<AccountingEntry> toAccountingEntryset(
            AccountSide accountSide,
            String accounts,
            String amounts
    ) {
        Set<AccountingEntry> ys = new HashSet<>();
        String[] as = accounts.split(",");
        String[] bs = amounts.split(",");
        for (int i = 0; i < as.length; i++) {
            ys.add(
                    // BigDecimal amount, String accountNumber, AccountSide accountSide
                    new AccountingEntry(
                            new BigDecimal(bs[i]),
                            as[i],
                            accountSide
                    )
            );
        }
        return ys;
    }

    // Convenience function for raising validate errors.
    private String errMessage(String msg, String[] columns) {
        return msg + " on line with columns: " + Arrays.toString(columns);
    }

    private void logicErr(String[] columns) {
        throw new IllegalStateException(
                errMessage("logic error in code", columns));
    }

    private void validationErr(String msg, Throwable e, String[] columns) {
        throw new IllegalStateException(errMessage(msg, columns), e);
    }

    public enum EventType {ACCOUNT, TRANSACTION}
}
