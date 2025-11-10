############################
# Stage 1: Build
############################
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copia só pom para otimizar cache de dependências
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests dependency:go-offline

# Copia código fonte
COPY src ./src

# Compila aplicação
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests clean package

############################
# Stage 2: Runtime
############################
FROM eclipse-temurin:21-jre-alpine AS runtime

# Variáveis de ambiente básicas
ENV APP_HOME=/app
WORKDIR $APP_HOME

# Cria usuário não-root
RUN addgroup -S app && adduser -S app -G app

# Copia jar construído
COPY --from=build /workspace/target/*-SNAPSHOT.jar app.jar

# Exposição de porta
EXPOSE 8080

USER app

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
