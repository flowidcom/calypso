FROM ubuntu:14.04
MAINTAINER Calin Groza "https://github.com/flowidcom/calypso"
RUN apt-get update
RUN apt-get install -y nginx
ADD src/main/webapp/index.html /usr/share/nginx/html/index.html
EXPOSE 80
