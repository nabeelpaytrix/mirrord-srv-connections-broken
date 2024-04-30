FROM eclipse-temurin:17-jre

EXPOSE 8080

COPY target/mirrord-srv-connections-broken-*.jar /opt/app/mirrord-srv-connections-broken.jar

ENTRYPOINT ["java","-jar","/opt/app/mirrord-srv-connections-broken.jar"]
