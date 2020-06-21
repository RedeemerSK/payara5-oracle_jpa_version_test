#!/bin/sh
SETUP_DIR=/home/oracle/setup
LOG_DIR=/home/oracle/setup/log
SETUP_LOG=$LOG_DIR/setupDB.log
SCHEMA_LOG=$LOG_DIR/setupSchema.log
BASH_RC=/home/oracle/.bashrc

# run original setupDb
/bin/bash $SETUP_DIR/setupDB.orig.sh 2>&1

if grep -q ": Error" $SETUP_LOG
then
    exit 1
fi

echo "[payara5-on-java11-jpa-test] setup schema ......"
echo "[payara5-on-java11-jpa-test] log file is : $SCHEMA_LOG"

source $BASH_RC
sqlplus / as sysdba @$SETUP_DIR/_setupSchema.sql 2>&1 >> $SCHEMA_LOG

echo "[payara5-on-java11-jpa-test] done at $SECONDS sec"

exit 0