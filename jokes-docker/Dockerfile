FROM ubuntu:latest

#tzdata
ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update && apt-get install -y supervisor  openjdk-11-jre

COPY conf/supervisord.conf /etc/supervisor/conf.d/supervisord.conf
COPY jar/jokes*SNAPSHOT.jar jokes.jar

CMD ["/usr/bin/supervisord"]
