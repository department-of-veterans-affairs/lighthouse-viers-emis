#!/usr/bin/env bash

# Grab AWS s3 bucket
aws s3 cp s3://emis-v2-dev-dependencies/Keystore/AllJavaKeystore /opt/va/certs 
aws s3 cp s3://emis-v2-dev-dependencies/Truststore/cacerts /opt/va/certs
aws s3 cp s3://emis-v2-dev-dependencies/client.properties /opt/va/
java -jar /opt/va/emis-api.jar
