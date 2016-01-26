FROM java:latest
MAINTAINER Alberto Lorente <a.lorenteleal@gmail.com>

ADD target/user-statistics-service-1.0-SNAPSHOT.jar /data/user-statistics-service.jar
ADD conf/docker-config.yaml /data/config.yaml

RUN apt-get update
#Install & configure Supervisor
RUN echo "Installing Supervisor for Docker container"
RUN apt-get -y install supervisor
RUN mkdir -p /var/log/supervisor
ADD supervisord.conf /etc/supervisor/conf.d/supervisord.conf

EXPOSE 8080
EXPOSE 8081

#Run Supervisor
CMD ["/usr/bin/supervisord"]