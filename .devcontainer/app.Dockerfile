# BUILD ARGUMENTS [ --build-args NAME=VALUE ]
ARG BUILD_VERSION=1.0.0
#ARG USERNAME=""
#ARG APP_DIR=""
#ARG UPGRADE_PACKAGES=""
#ARG INSTALL_GRADLE=""
#ARG GIT_USER=""
#ARG GIT_EMAIL=""
#ARG GIT_REPO=""
#ARG GIT_BRANCH=""
#ARG JAVA_HOME=""
#ARG ADDITIONAL_PACKAGES=""

FROM  dalexander2israel/spring_security_web_service:${BUILD_VERSION}
WORKDIR /app
CMD ["java","-jar","spring_security_web_service.jar"]