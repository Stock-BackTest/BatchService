FROM openjdk:25-ea-jdk-slim

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 비루트 사용자
RUN useradd -m -u 10001 appuser

WORKDIR /app

# 빌드한 JAR 복사
COPY build/libs/*.jar app.jar

# 로그 디렉토리
RUN mkdir -p /app/logs && chown -R appuser:appuser /app

USER appuser

# JVM 옵션
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
