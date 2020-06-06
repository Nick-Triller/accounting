import core.account.Account;
import core.account.AccountingEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;

import static core.account.AccountSide.CREDIT;
import static core.account.AccountSide.DEBIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {

    @Test
    public void testInvalidAccountNumber() {
        // Arrange
        Account cash = new Account("000001", "Cash", DEBIT);
        AccountingEntry entry = new AccountingEntry(new BigDecimal(50), "WRONG-ACCOUNT-NUMBER", DEBIT);
        // Act
        Executable act = () -> cash.addEntry(entry);
        // Assert
        assertThrows(IllegalArgumentException.class, act);
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
        assertEquals(new BigDecimal(50), cash.getBalance());
        assertEquals(new BigDecimal(50), loan.getBalance());
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
        assertEquals(new BigDecimal(50), cash.getBalance());
        assertEquals(new BigDecimal(-50), checking.getBalance());
    }
}
