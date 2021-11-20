#!/bin/bash
mysql -P 3306 -h localhost -u root -proot luoshu < $LUOSHU_HOME/bin/luoshu.sql
