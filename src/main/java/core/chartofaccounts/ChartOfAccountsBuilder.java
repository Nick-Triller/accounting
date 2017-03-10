package core.chartofaccounts;

import core.account.AccountDetails;
import core.account.AccountDetailsImpl;
import core.account.AccountSide;

import java.util.HashSet;
import java.util.Set;

public class ChartOfAccountsBuilder {
    private Set<AccountDetails> accountDetails = new HashSet<>();

    private ChartOfAccountsBuilder() {
    }

    public static ChartOfAccountsBuilder create() {
        return new ChartOfAccountsBuilder();
    }

    public ChartOfAccountsBuilder addAccount(String accountNumber, String name, AccountSide increaseSide) {
        AccountDetails accountDetails = new AccountDetailsImpl(accountNumber, name, increaseSide);
        this.accountDetails.add(accountDetails);
        return this;
    }

    public ChartOfAccounts build() {
        return new ChartOfAccounts(accountDetails);
    }
}
