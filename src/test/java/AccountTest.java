import core.account.Account;
import core.account.AccountingEntry;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;

public class AccountTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAccountNumber() {
        // Arrange
        Account cash = new Account("000001", "Cash", DEBIT);
        // Act + Assert
        cash.addEntry(new AccountingEntry(new BigDecimal(50), "WRONG-ACCOUNT-NUMBER", DEBIT));
    }

    @Test
    public void testGetBalanceIncreaseIncrease() {
        // Arrange
        String cashAccountNumber = "000001";
        String loanAccountNumber = "000002";

        Account cash = new Account(cashAccountNumber, "Cash", DEBIT);
        Account loan = new Account(loanAccountNumber, "Loan", CREDIT);

        AccountingEntry e1 = new AccountingEntry(new BigDecimal(50), cashAccountNumber, DEBIT);
        AccountingEntry e2 = new AccountingEntry(new BigDecimal(50), loanAccountNumber, CREDIT);

        // Act
        cash.addEntry(e1);
        loan.addEntry(e2);

        // Assert
        Assert.assertEquals(new BigDecimal(50), cash.getBalance());
        Assert.assertEquals(new BigDecimal(50), loan.getBalance());
    }

    @Test
    public void testGetBalanceIncreaseDecrease() {
        // Arrange
        String cashAccountNumber = "000001";
        String checkingAccountNumber = "000002";
        Account cash = new Account(cashAccountNumber, "Cash", DEBIT);
        Account checking = new Account(checkingAccountNumber, "Checking", DEBIT);

        AccountingEntry e1 = new AccountingEntry(new BigDecimal(50), cashAccountNumber, DEBIT);
        AccountingEntry e2 = new AccountingEntry(new BigDecimal(50), checkingAccountNumber, CREDIT);

        // Act
        cash.addEntry(e1);
        checking.addEntry(e2);

        // Assert
        Assert.assertEquals(new BigDecimal(50), cash.getBalance());
        Assert.assertEquals(new BigDecimal(-50), checking.getBalance());
    }
}
