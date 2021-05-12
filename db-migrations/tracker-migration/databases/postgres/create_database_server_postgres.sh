#!/bin/bash

docker run --name tracker_db -e POSTGRES_PASSWORD=password -d -p 5432:5432 -v $HOME/workspace/tracker/db-migrations/tracker-migration/databases/:/tmp/databases postgres
docker exec tracker_db psql -U postgres -f /tmp/databases/create_databases_postgres.sql -d tracker_db