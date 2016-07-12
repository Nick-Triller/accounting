package core.account;

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents an immutable account description.
 */
final public class AccountDetails {
    private final String accountNumber;
    private final AccountSide increaseSide;
    private final String name;

    public AccountDetails(String accountNumber, String name, AccountSide increaseSide) {
        this.accountNumber = checkNotNull(accountNumber);
        this.increaseSide = checkNotNull(increaseSide);
        this.name = checkNotNull(name);
        checkArgument(!accountNumber.isEmpty());
        checkArgument(!name.isEmpty());
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public AccountSide getIncreaseSide() {
        return increaseSide;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("accountNumber", accountNumber)
                .add("name", name)
                .add("increaseSide", increaseSide)
                .toString();
    }
}
