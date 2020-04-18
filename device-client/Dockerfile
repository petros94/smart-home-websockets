FROM openjdk:13-alpine
VOLUME /tmp
ADD target/device-client.jar app.jar
ENV JAVA_OPTS="-Xms64m -Xmx128m --enable-preview"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
