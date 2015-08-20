Docker Build Instructions
=========================

This folder contains the configuration for building Docker containers.

Initalize Boot2Docker

    boot2docker init

Start the Docker host

    boot2docker start

Add a new entry in the port forwarding for Boot2Docker: host 10080 -> boot2docker 80. On Mac this is done using the following command:

    VBoxManage modifyvm "boot2docker-vm" --natpf1 "tcp-port1080,tcp,,10080,,80"

Connect to the host

    boot2docker ssh

Build the static_web image using nginx

    export PS1="\h# \w\n\$ "
    cd ~/git/calypso/calypso-ws
      
    mkdir -p target/static_web ### This is the Docker context, all the files that required for the build are in this directory
    cd target/static_web
    cp ../../src/main/docker/static_web.dockerfile Dockerfile
    cp ../../src/main/webapp/index.html .
    docker build -t=flowid/static_web .
    
    sudo docker run -d -p 10080:80 --name static_web flowid/static_web nginx -g "daemon off;"

Build the tomcat application

    cd /c/Users/calin/git/calypso/calypso-ws
    mkdir -p target/tomcat
    cp target/calypso-ws.war target/tomcat
    cd target/tomcat
    cp ../../src/main/docker/tomcat.dockerfile Dockerfile
    docker build -t=flowid/tomcat .
    sudo docker run -d -p 9999:8080 --name tomcat flowid/tomcat
    

View network information

    sudo iptables -L
    