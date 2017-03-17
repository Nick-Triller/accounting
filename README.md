# Accounting
Accounting is an in-memory double-entry bookkeeping component written in Java.

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

## Setup

Accounting uses [lombok](https://projectlombok.org/) to reduce getter and setter code clutter.

## License

MIT License

Copyright (c) 2017 Nick Triller

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
