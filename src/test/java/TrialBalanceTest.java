import core.Journal;
import core.Ledger;
import core.TrialBalanceResult;
import core.chartofaccounts.ChartOfAccounts;
import core.chartofaccounts.ChartOfAccountsBuilder;
import core.transaction.AccountingTransaction;
import core.transaction.AccountingTransactionBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static core.account.AccountSide.DEBIT;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrialBalanceTest {
    @Test
    public void testTrialBalanceIsBalanced() {
        // Arrange
        String cashAccountNumber = "000001";
        String checkingAccountNumber = "000002";

        ChartOfAccounts coa = ChartOfAccountsBuilder.create()
                .addAccount(cashAccountNumber, "Cash", DEBIT)
                .addAccount(checkingAccountNumber, "Cash", DEBIT)
                .build();
        Journal journal = new Journal();

        // Deposit cash in the bank
        AccountingTransaction t1 = AccountingTransactionBuilder.create()
                .debit(new BigDecimal(222), checkingAccountNumber)
                .credit(new BigDecimal(222), cashAccountNumber)
                .build();
        journal.addTransaction(t1);
        Ledger ledger = new Ledger(journal, coa);

        // Act
        TrialBalanceResult trialBalance = ledger.computeTrialBalance();

        // Assert
        assertTrue(trialBalance.isBalanced());
    }
}
