FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /work
COPY . .
RUN ./gradlew bootJar

FROM eclipse-temurin:17-jre-jammy
LABEL authors="sh-kang"
WORKDIR /app

# install dependencies
RUN apt-get update && apt-get upgrade -y && apt-get install -y python3 ffmpeg && apt-get clean -y && \
    curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/bin/yt-dlp && \
    chmod a+rx /usr/bin/yt-dlp

#RUN curl -L https://github.com/yt-dlp/FFmpeg-Builds/releases/download/latest/ffmpeg-master-latest-linux64-gpl.tar.xz \
#    -o ffmpeg.tar.xz && \
#    mkdir ffmpeg && tar -xf ffmpeg.tar.xz --strip-components=1 -C ffmpeg/ && rm -rf ffmpeg.tar.xz && \
#    chmod a+rx ffmpeg/bin/* && \
#    ln -s $(pwd)/ffmpeg/bin/ffmpeg /usr/bin/ffmpeg && \
#    ln -s $(pwd)/ffmpeg/bin/ffprobe /usr/bin/ffprobe

COPY --from=builder /work/build/libs/*.jar /app/yt-media-extractor-api.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV APP_BASE_PATH=/app/work
RUN mkdir work
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/yt-media-extractor-api.jar"]
