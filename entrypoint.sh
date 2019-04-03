#!/usr/bin/env bash

cd /tmp
cp -rf /bucket/. /tmp/src/main/resources
mvn clean verify
java -jar ./target/emis-api-1.0-SNAPSHOT.jar