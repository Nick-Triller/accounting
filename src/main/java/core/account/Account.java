package core.account;

import com.google.common.base.MoreObjects;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents an account with entries.
 */
final public class Account {
    final private List<AccountingEntry> entries = new ArrayList<>();

    @Getter
    final private AccountDetails accountDetails;

    public Account(AccountDetails accountDetails) {
        this.accountDetails = checkNotNull(accountDetails);
    }

    public Account(String accountNumber, String name, AccountSide increaseSide) {
        this.accountDetails = new AccountDetailsImpl(accountNumber, name, increaseSide);
    }

    /**
     * Adds an entry to the account.
     *
     * @param entry A debit or credit entry
     */
    public void addEntry(AccountingEntry entry) {
        checkNotNull(entry);
        checkArgument(entry.getAccountNumber().equals(accountDetails.getAccountNumber()));
        entries.add(entry);
    }

    /**
     * Returns the debit/credit balance with consideration of the increase side
     *
     * @return Balance
     */
    public BigDecimal getBalance() {
        BigDecimal signum = accountDetails.getIncreaseSide() == AccountSide.DEBIT
                ? BigDecimal.ONE : BigDecimal.ONE.negate();
        return getRawBalance().multiply(signum);
    }

    /**
     * Returns the debit/credit balance without consideration of the increase side
     *
     * @return Balance
     */
    public BigDecimal getRawBalance() {
        return entries.stream()
                .map(e -> e.getAccountSide() == AccountSide.DEBIT ? e.getAmount() : e.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("entries", entries)
                .add("accountDetails", accountDetails)
                .toString();
    }
}
