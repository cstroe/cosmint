#!/bin/sh

mvn -q exec:java \
  -Dexec.mainClass="com.github.cstroe.spendhawk.cli.AccountManager" \
  -Dexec.args="create Chase Bank"
mvn -q exec:java \
  -Dexec.mainClass="com.github.cstroe.spendhawk.cli.TransactionManager" \
  -Dexec.args="create 1 11.99 010101000000 Test Transaction 1"

mvn -q exec:java \
  -Dexec.mainClass="com.github.cstroe.spendhawk.cli.AccountManager" \
  -Dexec.args="list"
mvn -q exec:java \
  -Dexec.mainClass="com.github.cstroe.spendhawk.cli.TransactionManager" \
  -Dexec.args="list"
