FROM openjdk:8-jdk-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="-Xmx2048m -Xms256m"

ADD target/AnalyticsFiles-*.jar /app.jar

CMD echo "The application will starting now..." && \
    sleep 0 && \
    java ${JAVA_OPTS} \
    -Dspring.cloud.bus.enabled=true \
    -Dspring.cloud.bootstrap.enabled=true \
    -Dspring.cloud.discovery.enabled=true \
    -Dspring.cloud.consul.enabled=true \
    -Dspring.cloud.consul.config.enabled=true \
    -Dspring.cloud.config.discovery.enabled=true \
    -Djava.security.egd=file:/dev/./urandom -jar /app.jar

