FROM openjdk:8-jre-alpine

# necessary tools to run application
RUN apk update && apk add bash && apk add curl && apk add --update py-pip

COPY target/emis-api-*.jar /opt/va/emis-api.jar

# Setup AWS s3 environment
ENV AWS_ACCESS_KEY_ID=unset
ENV AWS_SECRET_ACCESS_KEY=unset
ENV AWS_DEFAULT_REGION=unset

# Get AWS
ADD "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" /tmp/aws2/awscli-bundle.zip
RUN cd /tmp/aws2 && chmod 777 /tmp/aws2/awscli-bundle.zip && unzip /tmp/aws2/aws*.zip && /tmp/aws2/awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws

# Copy entrypoint script into the temporary folder
COPY /entrypoint.sh /tmp/entrypoint.sh

# File executable permissions seem to get lost when building the image on windows
RUN chmod 777 /tmp/entrypoint.sh

# Start Up
ENTRYPOINT ["/tmp/entrypoint.sh"]
