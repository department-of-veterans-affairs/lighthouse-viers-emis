FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
USER root

RUN apk add python

# Get AWS
ADD "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" /tmp/aws2/awscli-bundle.zip
RUN cd /tmp/aws2 && chmod 777 /tmp/aws2/awscli-bundle.zip && unzip /tmp/aws2/aws*.zip && /tmp/aws2/awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws

# Setup AWS s3 environment
ENV AWS_ACCESS_KEY_ID=AKIAKVAUIXMVPL5WHULQ AWS_SECRET_ACCESS_KEY=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 AWS_DEFAULT_REGION=us-gov-west-1 ISC_PACKAGE_INSTANCENAME="CACHE" ISC_PACKAGE_INSTALLDIR="/opt/cache" ISC_PACKAGE_STARTCACHE="N"

# Grab AWS s3 bucket
RUN aws s3 sync s3://emis-v2-dev-dependencies/ /bucket/.