#!/bin/bash


Server=`ps -ef | grep java | grep com.cy.onepush.App | grep -v grep | awk '{print $2}'`
if [[ $Server -gt 0 ]]; then
  kill -9 $Server
else
  echo "[Luoshu Server] System did not run."
fi
