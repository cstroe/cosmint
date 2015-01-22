# SpendHawk

A webapp to watch your spending like a hawk.

## Running SpendHawk

SpendHawk requires 
<a href="http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html">Java 8</a> 
installed.  The commands were tested on a Debian-based Linux system (my personal
recommendation is <a href="http://www.linuxmint.com/edition.php?id=174">Linux Mint Mate Edition</a>).
It also runs well in a <a href="https://www.virtualbox.org">Virtual Machine</a>.

1. Clone the SpendHawk repository somewhere on your computer:

  ```
  git clone https://github.com/cstroe/SpendHawk.git
  ```
  
2. In a new terminal window, change your directory to the SpendHawk directory 
and start the hsqldb in server mode:
  
  ```
  cd SpendHawk
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
  cd SpendHawk
  mvn package
  mkdir wildfly
  cd wildfly
  ln -s ../target/spendhawk.war
  cd ..
  mvn wildfly:run -Dwildfly.deployment.targetDir=wildfly
  ```
  
  NOTE: If you would like to attach a debugger to the WildFly server, see below.
  
  The reason for creating a separate directory for the wildfly server is so that
  we can clean, rebuild, and redeploy the application without having to restart 
  the WildFly server that is running.
  
  NOTE: This will deploy the application.
  
4. Because the previous step deployed the application, you can browse to:
  
  ```
  http://localhost:8080/spendhawk
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
  -Dwildfly.jvmArgs="-Xms64m -Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8787"
  ```
  
  WildFly will open a debugging port at 8787 to which you can connect from your
  IDE.