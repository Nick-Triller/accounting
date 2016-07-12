package core;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import core.account.Account;
import core.account.AccountDetails;
import core.account.AccountingEntry;
import core.chartofaccounts.ChartOfAccounts;
import core.transaction.AccountingTransaction;
import core.transaction.AccountingTransactionBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a set of accounts and their transactions.
 */
final public class Ledger {
    final private HashMap<String, Account> accountNumberToAccount = new HashMap<>();
    final private Journal journal = new Journal();
    final private ChartOfAccounts coa;

    public Ledger(ChartOfAccounts coa) {
        this.coa = coa;
        // Create coa accounts
        coa.getAccountNumberToAccountDetails().values().forEach(ad -> addAccount(ad));
    }

    public Ledger(Journal journal, ChartOfAccounts coa) {
        this(coa);
        // Add transactions
        journal.getTransactions().forEach(t -> this.commitTransaction(t));
    }

    public AccountingTransactionBuilder createTransaction(Map<String, String> info) {
        return AccountingTransactionBuilder.create(info);
    }

    public void commitTransaction(AccountingTransaction transaction) {
        // Add entries to accounts
        transaction.getEntries().forEach(e -> this.addAccountEntry(e));
        journal.addTransaction(transaction);
    }

    public TrialBalanceResult computeTrialBalance() {
        return new TrialBalanceResult(Sets.newHashSet(accountNumberToAccount.values()));
    }

    public Journal getJournal() {
        return journal;
    }

    public BigDecimal getAccountBalance(String accountNumber) {
        return accountNumberToAccount.get(accountNumber).getBalance();
    }

    private void addAccount(AccountDetails accountDetails) {
        if (accountNumberToAccount.containsKey(accountDetails.getAccountNumber()))
            throw new IllegalArgumentException();
        accountNumberToAccount.put(accountDetails.getAccountNumber(), new Account(accountDetails));
    }

    private void addAccountEntry(AccountingEntry entry) {
        accountNumberToAccount.get(entry.getAccountNumber()).addEntry(entry);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("accountNumberToAccountMap", accountNumberToAccount)
                .add("journal", journal)
                .add("chartOfAccounts", coa)
                .toString();
    }
}
