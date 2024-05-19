#!/bin/bash
./gradlew clean && ./gradlew bootJar
docker build -t yt-media-extractor-api:latest .
