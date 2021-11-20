#!/bin/bash

#start server

if [ -z "$LUOSHU_HOME" ]; then
  echo "LUOSHU_HOME not found"
  echo "Please export LUOSHU_HOME to your environment variable"
  exit
fi

cd $LUOSHU_HOME
Lib_dir=`ls | grep lib`
if [ -z "$Lib_dir" ]; then
  echo "Invalid LUOSHU_HOME"
  exit
fi

Server=`ps -ef | grep java | grep com.cy.onepush.App | grep -v grep | awk '{print $2}'`
if [[ $Server -gt 0 ]]; then
  echo "[Luoshu Server] is already started"
  exit
fi

cd $LUOSHU_HOME
TODAY=`date "+%Y-%m-%d"`
LOG_PATH=$LUOSHU_HOME/logs/sys/davinci.$TODAY.log
nohup java -Dfile.encoding=UTF-8 -cp $JAVA_HOME/lib/*:lib/* com.cy.onepush.App > $LOG_PATH  2>&1 &

echo "=========================================="
echo "Starting..., press \`CRTL + C\` to exit log"
echo "=========================================="

sleep 3s
tail -f $LOG_PATH