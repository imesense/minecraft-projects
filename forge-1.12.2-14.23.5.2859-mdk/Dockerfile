FROM openjdk:8-jdk-alpine AS build

WORKDIR /app

COPY . .

RUN \
    ./gradlew build

FROM scratch AS export

COPY --from=build \
    /app/build/libs/*.jar \
    /build/libs/
