package core.account;

import com.google.common.base.MoreObjects;
import core.transaction.AccountingTransaction;
import lombok.Getter;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.*;

/**
 * Represents an Accounting Entry.
 * The transaction reference is set automatically when an AccountingEntry is passed to the transaction constructor.
 * Once the transaction is set, it can't be changed.
 */
public final class AccountingEntry {

    @Getter
    final private BigDecimal amount;

    @Getter
    final private AccountSide accountSide;

    @Getter
    final private String accountNumber;

    private AccountingTransaction transaction;
    // Indicates if the transaction was set
    private boolean freeze = false;

    public AccountingEntry(BigDecimal amount, String accountNumber, AccountSide accountSide) {
        this.amount = checkNotNull(amount);
        this.accountNumber = checkNotNull(accountNumber);
        this.accountSide = checkNotNull(accountSide);
        checkArgument(amount.signum() == 1, "Accounting entries can't have a negative amount");
        checkArgument(!accountNumber.isEmpty());
    }

    /**
     * Gets the associated transaction.
     * Throws a NullPointerException if no transaction is associated.
     *
     * @return Associated transaction
     */
    public AccountingTransaction getTransaction() {
        checkNotNull(transaction, "Getter returning null. You have to set a transaction.");
        return transaction;
    }

    /**
     * This setter is required to enable circular references between entries and transactions.
     *
     * @param transaction The transaction belonging to this entry
     */
    public void setTransaction(AccountingTransaction transaction) {
        checkState(!freeze, "An AccountingEntry's transaction can only be set once");
        checkNotNull(transaction);
        this.transaction = transaction;
        freeze = true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("amount", amount.toString())
                .addValue(accountSide)
                .toString();
    }
}
