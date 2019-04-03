FROM openjdk:8-jre-slim
VOLUME /tmp
EXPOSE 8080
USER root

RUN apt-get clean && \
    apt-get update && \
    apt-get -qy install \
                maven \
				openjdk-8-jdk
				
RUN apt-get -y install python

# Get AWS
ADD "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" /tmp/aws2/awscli-bundle.zip
RUN cd /tmp/aws2 && chmod 777 /tmp/aws2/awscli-bundle.zip && unzip /tmp/aws2/aws*.zip && /tmp/aws2/awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws

# Setup AWS s3 environment
ENV AWS_ACCESS_KEY_ID=AKIAKVAUIXMVPL5WHULQ AWS_SECRET_ACCESS_KEY=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 AWS_DEFAULT_REGION=us-gov-west-1 ISC_PACKAGE_INSTANCENAME="CACHE" ISC_PACKAGE_INSTALLDIR="/opt/cache" ISC_PACKAGE_STARTCACHE="N"

COPY / /tmp

# Grab AWS s3 bucket
RUN aws s3 cp s3://emis-v2-dev-dependencies/Keystore/AllJavaKeystore /bucket/Keystore/
RUN aws s3 cp s3://emis-v2-dev-dependencies/Keystore/facade_keystore.jks /bucket/Keystore/
RUN aws s3 cp s3://emis-v2-dev-dependencies/Truststore/cacerts /bucket/Truststore/
RUN aws s3 cp s3://emis-v2-dev-dependencies/client.properties /bucket/

# File executable permissions seem to get lost when building the image on windows
RUN chmod 777 /tmp/entrypoint.sh

# Start Up
ENTRYPOINT ["/tmp/entrypoint.sh"]