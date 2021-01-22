#!/bin/sh

set COUNT_SQL_STATEMENT="select Count(*) from table where key=1"
set CROSSCHECK_COUNT_SQL_STATEMENT="select Count(*) from table where key=1"
set ENDPOINT_URL="http://www.google.com"
set SQL_USER="username"
set SQL_PASS="password"
set SQL_URL="URL"

java -jar target/excel-reader.0.0.1-jar-with-dependencies.jar fileName