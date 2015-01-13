#!/bin/sh

mvn exec:java -Dexec.mainClass="org.hsqldb.Server" -Dexec.args="-database.0 file:data/testing"
