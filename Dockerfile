FROM eclipse-temurin:17-jre-ubi9-minimal
LABEL authors="sh-kang"
WORKDIR /app
RUN microdnf update && microdnf upgrade -y && microdnf install -y python3 xz && \
    curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/bin/yt-dlp && \
    curl -L https://github.com/yt-dlp/FFmpeg-Builds/releases/download/latest/ffmpeg-master-latest-linux64-gpl.tar.xz \
    -o /app/ffmpeg-master-latest-linux64-gpl.tar.xz && \
    chmod a+rx /usr/bin/yt-dlp && \
    tar -xvf ffmpeg-master-latest-linux64-gpl.tar.xz && rm -rf ffmpeg-master-latest-linux64-gpl.tar.xz && \
    chmod a+rx ffmpeg-master-latest-linux64-gpl/bin/* && \
    ln -s /app/ffmpeg-master-latest-linux64-gpl/bin/ffmpeg /usr/bin/ffmpeg && \
    ln -s /app/ffmpeg-master-latest-linux64-gpl/bin/ffprobe /usr/bin/ffprobe
RUN mkdir work
COPY build/libs/yt-media-extractor-api-*.jar /app/yt-media-extractor-api.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/yt-media-extractor-api.jar"]
