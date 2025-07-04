FROM maven:3.9.10-amazoncorretto-21-alpine AS build
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY . .
RUN mvn clean package -DskipTests

ARG DEPENDENCY=/build/target/dependency
RUN mkdir -p ${DEPENDENCY} && (cd ${DEPENDENCY}; jar -xf ../*.jar)

# Runnable image
FROM maven:3.9.10-amazoncorretto-21-alpine as runnable
VOLUME /tmp
VOLUME /logs
ARG DEPENDENCY=/build/target/dependency
# Create User&Group to not run docker images with root user
RUN useradd -ms /bin/bash awesome
USER awesome

# Copy libraries & meta-info & classes
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
# Run application
ENTRYPOINT ["java","-cp","app:app/lib/*","fr.hoophub.hoophubAPI.HoophubApiApplication"]