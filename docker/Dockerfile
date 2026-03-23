# OpenJDK 베이스 이미지
FROM eclipse-temurin:17 AS builder
WORKDIR workspace
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} catalog-service.jar
# 계층 Jar 모드를 적용해 아카이브에서 계층 추출
RUN java -Djarmode=layertools -jar catalog-service.jar extract


FROM eclipse-temurin:17
RUN useradd spring
USER spring
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./

# 스프링부트 런처를 사용해 우버 JAR 대신 계층으로 애플리케이션을 시작함
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
