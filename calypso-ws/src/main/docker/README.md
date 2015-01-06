Docker Build Instructions
=========================

This folder contains the configuration for building Docker containers.

## Start the Docker host

boot2docker start
boot2docker stop
Add a new entry in the port forwarding for Boot2Docker:
  host 9999 -> boot2docker 9999

## Coonnect to the host

boot2docker ssh

## Build the static_web image

export PS1="\h# \w\n\$ "
cd /c/Users/calin/git/calypso/calypso-ws

mkdir -p target/static_web
cd target/static_web
cp ../../src/main/docker/static_web.dockerfile Dockerfile
docker build -t=flowid/static_web .
sudo docker run -d -p 9999:80 --name static_web flowid/static_web nginx -g "daemon off;"

## Build the tomcat application

cd /c/Users/calin/git/calypso/calypso-ws
mkdir -p target/tomcat
cp target/calypso-ws.war target/tomcat
cd target/tomcat
cp ../../src/main/docker/tomcat.dockerfile Dockerfile
docker build -t=flowid/tomcat .
sudo docker run -d -p 9999:8080 --name tomcat flowid/tomcat

## View network information

sudo iptables -L
