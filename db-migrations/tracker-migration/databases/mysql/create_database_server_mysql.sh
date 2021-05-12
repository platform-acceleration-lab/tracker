#!/bin/bash

docker run --name tracker_db -e MYSQL_ROOT_PASSWORD=password -d -p 3306:3306 -v $HOME/workspace/tracker/db-migrations/tracker-migration/databases/:/tmp/databases mysql:latest

