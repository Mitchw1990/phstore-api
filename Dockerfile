FROM java:8-jre
MAINTAINER Mitch Warrenburg <mitchwarrenburg@yahoo.com>

ADD build/libs/phstore-api-1.0.jar /app/
ADD bitprobe-482adb943afd.json /app/

ENV GOOGLE_APPLICATION_CREDENTIALS /app/bitprobe-482adb943afd.json
ENV SPRING_PROFILES_ACTIVE cloud

CMD ["java", "-Xmx200m", "-jar", "/app/phstore-api-1.0.jar"]

EXPOSE 8080