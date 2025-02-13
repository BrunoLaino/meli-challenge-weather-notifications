# Imagem base do Maven para build
FROM maven:3.8.7-eclipse-temurin-17 AS builder
WORKDIR /app

# Copia o pom.xml
COPY pom.xml .
# Baixa dependências (para cache)
RUN mvn dependency:go-offline

# Copia o código fonte
COPY src ./src

# Compila o projeto
RUN mvn clean package -DskipTests

# ----------------------------------------------------------------
# Imagem para produção
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o JAR gerado
COPY --from=builder /app/target/api-notifications-1.0.0.jar app.jar

# Exponde a porta da aplicação
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
