#!/bin/bash

# Перед запуском этого скрипта нужно из-под папки java выполнить ./mvnw clean package

docker compose -f monitor/docker-compose.yml up -d --build
# Pause for 8 seconds
sleep 8

docker compose -f java/ClientAuth/docker-compose.yml up -d --build
docker compose -f java/ClientStorage/docker-compose.yml up -d --build
docker compose -f java/DeviceStorage/docker-compose.yml up -d --build
docker compose -f java/MainManagerOutput/docker-compose.yml up -d --build
docker compose -f java/ManagerInput/docker-compose.yml up -d --build
docker compose -f java/verifier/docker-compose.yml up -d --build

docker compose -f hub/docker-compose.yml up -d --build
docker compose -f mainHub/docker-compose.yml up -d --build
docker compose -f mainStorage/docker-compose.yml up -d --build
docker compose -f storage/docker-compose.yml up -d --build
docker compose -f cloudService/docker-compose.yml up -d --build
