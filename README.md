#Accounting
Accounting is a in-memory double-entry bookkeeping library.

## Usage example
```Java
String cashAccountNumber = "000001";
String checkingAccountNumber = "000002";
String accountsReceivableAccountNumber = "000003";

// Chart of accounts describes the available accounts
ChartOfAccounts coa = ChartOfAccountsBuilder.create()
        .addAccount(cashAccountNumber, "Cash", DEBIT)
        .addAccount(checkingAccountNumber, "Checking", DEBIT)
        .addAccount(accountsReceivableAccountNumber, "Accounts Receivable", DEBIT)
        .build();
// Setup ledger
Ledger ledger = new Ledger(coa);

// Accounts Receivable 35 was settled with cash 10 and wire transfer 25
AccountingTransaction t = ledger.createTransaction(null)
        .debit(new BigDecimal(10), cashAccountNumber)
        .debit(new BigDecimal(25), checkingAccountNumber)
        .credit(new BigDecimal(35), accountsReceivableAccountNumber)
        .build();
ledger.commitTransaction(t);

// Print ledger
System.out.println(ledger.toString());
```