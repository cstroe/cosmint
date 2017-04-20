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
recommendation is <a href="http://www.linuxmint.com/edition.php?id=174">Linux Mint Mate Edition</a>).
It also runs well in a <a href="https://www.virtualbox.org">Virtual Machine</a>.

1. Clone the SpendHawk repository somewhere on your computer:

  ```
  git clone https://github.com/cstroe/spendhawk-java.git spendhawk
  ```
  
2. In a new terminal window, change your directory to the SpendHawk directory 
and start the hsqldb in server mode:
  
  ```
  cd spendhawk/modules/web
  mvn exec:java -Dexec.mainClass="org.hsqldb.Server" -Dexec.args="-database.0 file:data/testing"
  ```
  
  This will save the database files to the data directory.  The database server 
  needs to be running while you use the app.  Because the server is writing to 
  files, you can close the server and restart it using the same command and your 
  data will persist between runs.
  
  NOTE: SpendHawk currently has a bug where if the database server is not running
  and we try to access it, the app will be left in a bad state, and subsequent 
  database access requests will fail even if the server is back up.  Just 
  redeploy the app to fix this.
  
3. In another new terminal window, we need to create the SpendHawk war, make a 
temporary directory for our standalone WildFly server and start the server:

  ```
  cd spendhawk/modules/web
  mvn package
  mkdir wildfly
  cd wildfly
  ln -s ../target/spendhawk-web.war
  cd ..
  mvn wildfly:run -Dwildfly.deployment.targetDir=wildfly \
  -Dwildfly.version=9.0.0.Final
  ```
  
  NOTE: If you would like to attach a debugger to the WildFly server, see below.
  
  The reason for creating a separate directory for the wildfly server is so that
  we can clean, rebuild, and redeploy the application without having to restart 
  the WildFly server that is running.
  
  NOTE: This will deploy the application.
  
4. Because the previous step deployed the application, you can browse to:
  
  ```
  http://localhost:8080/spendhawk-web
  ```
  
  and access the SpendHawk webapp.
  
5. You can make changes to the code and then in a new terminal issue:

  ```
  mvn clean wildfly:deploy
  ```
  
  to redeploy the application and see your changes.
  
## Debugging

To debug SpendHawk, you need to start the WildFly server with a listening debugger
session.  Use the following command instead of the normal one from step 3 above:

  ```
  mvn wildfly:run -Dwildfly.deployment.targetDir=wildfly \
  -Dwildfly.version=9.0.0.Final \
  -Dwildfly.jvmArgs="-Xms64m -Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8787"
  ```
  
  WildFly will open a debugging port at 8787 to which you can connect from your
  IDE.

## Running Integration Tests

```
cd spendhawk/modules/web
cd wildfly/wildfly-run/wildfly-9.0.0.Final
bash ./bin/add-user.sh
```

Add user with username that matches `src/test/resources/arquillian.xml`.

Then you can run any of the integration tests (classes that end in `IT`) from 
any JUnit compatible IDE.
