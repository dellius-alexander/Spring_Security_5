ARG BUILD_VERSION="1.0.0"
FROM openjdk:18-ea-11-jdk-alpine
ENV BUILD_VERSION=${BUILD_VERSION}
RUN mkdir -p /app
COPY ./app/** /app/
RUN ls -R /app
WORKDIR /app
EXPOSE 8443
CMD ["java","-jar","spring_security_web_service-${BUILD_VERSION}.jar"]