#!/bin/bash
cd devcon-auth || return
./mvnw clean install -DskipTests
docker build -t docker.io/library/devcon-auth:0.0.1-SNAPSHOT .
cd ..
cd devcon-discovery || return
./mvnw spring-boot:build-image -DskipTests
cd ..
cd devcon-frontend || return
./mvnw spring-boot:build-image -DskipTests
cd ..
cd devcon-orders || return
./mvnw spring-boot:build-image -DskipTests
cd ..
cd devcon-payments || return
./mvnw spring-boot:build-image -DskipTests
cd ..
cd devcon-products || return
./mvnw spring-boot:build-image -DskipTests
cd ..
cd devcon-users || return
./mvnw spring-boot:build-image -DskipTests
cd ..
cd devcon-api-gateway || return
./mvnw spring-boot:build-image -DskipTests
cd ..