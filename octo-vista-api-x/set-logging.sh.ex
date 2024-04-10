#!/bin/sh

DEV_HOME=/c/dev
WILDFLY_HOME=$DEV_HOME/wildfly
PATH=$PATH:$JAVA_HOME/bin


LOG_LEVEL=DEBUG

if [ -n "$1" ]; then
  LOG_LEVEL=$1
fi

echo "setting logging level to $LOG_LEVEL"


$WILDFLY_HOME/bin/jboss-cli.sh --connect --commands="/subsystem=logging/logger=gov.va.octo.vista.api:write-attribute(name="level",value="$LOG_LEVEL"),/subsystem=logging/console-handler=CONSOLE:write-attribute(name="level",value="$LOG_LEVEL")"