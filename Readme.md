# RW796-Assignment1

[![Build Status](http://8e0a-146-232-108-171.ngrok.io/buildStatus/icon?job=rw796-assignment1)](http://173b-146-232-108-171.ngrok.io/job/rw796-assignment1/)

The following project forms part of an assignment for RW796. The goals were to demonstrate the student's understanding of continuous integration and continuous deployment, in particular setting up a build pipeline.

# Project
The project, a minimal string manipulation library, was created specifically for the purpose of this assignment.




# CI/CD setup
The tool used to implement CI/CD is Jenkins. More information about the pipeline and  CI/CD infrastructure provided below
## Pipeline
The declarative pipeline script can be found in the Jenkisnfile in the root directory of the repository. 
It is important to note that maven is used as the build system. The pipeline consists of 3 stages. In stage 1, the build stage,  sources are compiled using 'mvn compile'. In the second stage, the testing stage, unit tests are run using 'mvn test' and the JUnit reports are published. In the final stage, deployment stage, artifacts are created using 'mvn package' and are only deployed if the build succeeds.

## Jenkins and build Agent

Jenkins configured to run all builds in an ephemeral docker agent, meaning that a temporary docker container is automatically created every time a build is triggered. This container is then removed after the build has been completed.

### Creating and running Jenkins
 ```Dockerfile
FROM enkins/jenkins:jdk11
USER root
RUN apt-get update && apt-get install -y apt-transport-https \
       ca-certificates curl gnupg2 \
       software-properties-common
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN apt-key fingerprint 0EBFCD88
RUN add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/debian \
       $(lsb_release -cs) stable"
RUN apt-get update && apt-get install -y docker-ce-cli
USER jenkins
RUN jenkins-plugin-cli --plugins "blueocean:1.24.7 docker-workflow:1.26"
```
The Dockerfile above is used to create the Jenkins image and can be run as follows
```

docker run --name jenkins-blueocean --rm --detach \
  --network jenkins --env DOCKER_HOST=tcp://docker:2376 \
  --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 \
  --publish 8080:8080 --publish 50000:50000 \
  --volume jenkins-data:/var/jenkins_home \
  --volume jenkins-docker-certs:/certs/client:ro \
  --volume /var/run/docker.sock: /var/run/docker.sock \
  myjenkins-blueocean:1.1
```

### Creating the agent

```
FROM openjdk:11

ARG VERSION=4.10
ARG user=jenkins
ARG group=jenkins
ARG uid=1000
ARG gid=1000
ARG DEBIAN_FRONTEND=noninteractive
ARG AGENT_WORKDIR=/home/${user}/agent

LABEL Description="Jenkins agent with maven"

# Setup the jenkins group and user
RUN addgroup --gid ${gid} ${group}
RUN adduser --home /home/${user} --uid ${uid} --gid ${gid} ${user}


# Download agent.jar and set correct permissions
RUN apt-get update && apt-get -y --no-install-recommends install \
  curl apt-utils bash git git-lfs openssh-client openssl procps \
  && curl --create-dirs -fsSL -o /usr/share/jenkins/agent.jar https://repo.jenkins-ci.org/public/org/jenkins-ci/main/remoting/${VERSION}/remoting-${VERSION}.jar \
  && chmod 755 /usr/share/jenkins \
  && chmod 644 /usr/share/jenkins/agent.jar \
  && ln  -sf /usr/share/jenkins/agent.jar /usr/share/jenkins/slave.jar
# install maven
RUN apt-get -y install maven 
# clean apt caches
RUN apt-get autoremove -y && \
apt-get clean && \
rm -rf /var/lib/apt/lists/*
USER ${user}
ENV AGENT_WORKDIR=${AGENT_WORKDIR}
RUN mkdir /home/${user}/.jenkins && mkdir -p ${AGENT_WORKDIR}
# setup volumes
VOLUME /home/${user}/.jenkins
VOLUME ${AGENT_WORKDIR}
# set workdir
WORKDIR /home/${user}
```
The dockerfile above can be used to create the docker image for the build agent. The build agent can be configured by installing the Docker plugin in Jenkins and filling out the respective details for the agent template.

# Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

# License
[MIT](https://choosealicense.com/licenses/mit/)
