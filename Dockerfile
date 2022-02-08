FROM maven:3.8.4-openjdk-17 as base
WORKDIR /tmp/app
COPY checkstyle checkstyle
RUN --mount=type=cache,target=/root/.m2 \
    cd checkstyle && mvn -B package \
    && mvn -B install:install-file -DlocalRepositoryPath=/tmp/app/.m2 -Dfile=target/checkstyle.jar -DpomFile=pom.xml \
    && rm -rf target
COPY . .

FROM base as build
ARG BRANCH_NAME
RUN --mount=type=cache,target=/root/.m2 \
    --mount=type=cache,target=/root/.npm \
    mvn -B -U package -DextraLocalRepositoryPath=/tmp/app/.m2 -DnpmInstallArg=ci -Dgit.branch=$BRANCH_NAME \
    && cp forms/target/forms.jar forms \
    && rm -rf ui/node ui/node_modules ui/build target */target

FROM openjdk:17
RUN mkdir /var/log/app /var/derby
ENV LOG_DIR=/var/log/app
WORKDIR /opt/app
COPY --from=build /tmp/app/forms/forms.jar .
CMD ["java", "-Dwicket.ioc.useByteBuddy=true", "-Dderby.system.home=/var/derby", "-jar", "/opt/app/forms.jar"]
