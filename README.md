# SpendHawk

The design of SpendHawk is informed by the GnuCash documentation.  
It's a multi-tenancy web application (many users in the same database).  It uses
a double-entry accounting model, with split-transaction support.

## Accounts

Each user keeps track of many accounts.   Some accounts have real-world counterparts
such as checking accounts, credit cards, etc.
Other accounts don't have a real world account counterpart, but are used to 
categorize expenses in real-world accounts (part of the double-entry model).

Monetary value can flow in and out of an account, and these changes are recorded
in a list of _cash flows_ against the account.  The current value of the account
is the sum of all the cash flows recorded against it.

Accounts can be nested, with the parent inheriting the value of the sub-accounts 
(in addition to any cash flows).

### Account Hierarchy Example

For example, a user may have the following chart of accounts:

    Asset
      Bank Checking
      Bank Savings
    
    Credit
      Visa Card
    
    Expenses
      Utilities
      Groceries
      Other

The `Asset` account has two sub-accounts, `Bank Checking` and `Bank Savings`.
Likewise, the `Expenses` account has 3 sub-accounts, including `Utilities` and 
`Groceries`.

An example transaction, such as paying $10 at the grocery store using your debit
card, would involve recording a cash flow of -$10 in the `Bank Checking` account
and recording a cash flow of $10 in the `Groceries` account.

## Transactions and Cash Flows

In Spendhawk, a transaction is broken up into multiple _cash flows_.
Each cash flow has an amount that is applied to a specific account, on a 
specific date (the _effective date_).  Each cash flow also has a human readable 
_description_.

The value of all the cash flows in every transaction must sum to 0, otherwise the 
"books" are not balanced.  This implies that a transaction must have at least two 
cash flows, each having the negated amount of the other (a transaction with a 
single cash flow with zero value serves no practical purpose).

As mentioned above, paying $10 at the grocery store using your debit card would 
involve recording a cash flow of -$10 in the `Bank Checking` account and 
recording a cash flow of $10 in the `Groceries` account.  The transaction is 
balanced (cash flows add up to 0).

If you were to buy $20 worth of groceries with your credit card, you would record 
a cash flow of -$20 against the `Visa Card` account and a $20 cash flow against 
the `Groceries` account.

At the end of the month, you can look at the value of the `Groceries` account and
see that you have spent $30 in groceries this month.

# Developing Spendhawk

## Running SpendHawk

SpendHawk requires 
<a href="http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html">Java 8</a> 
installed.  The commands were tested on a Debian-based Linux system (my personal
recommendation is <a href="http://www.linuxmint.com">Linux Mint</a>).
It also runs well in a <a href="https://www.virtualbox.org">Virtual Machine</a>.

1. Clone the SpendHawk repository somewhere on your computer:

  ```
  git clone https://github.com/cstroe/spendhawk-java.git spendhawk
  ```
  
2. In a new terminal window, change your directory to the SpendHawk directory 
and start SpendHawk's dependencies:
  
  ```
  docker-compose up -d postgres
  ```
    
3. Now we can start SpendHawk:

  ```
  ./gradlew bootRun
  ```
    
4. To access SpendHawk, you can browse to:
  
  ```
  http://localhost:8080
  ```
  
## Links

* [GNU Cash User Guide](http://gnucash.org/viewdoc.phtml?doc=guide)
* [Debits and Credits](https://www.accountingtools.com/articles/2017/5/17/debits-and-credits)