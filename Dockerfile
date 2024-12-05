# Etapa de build
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copia os arquivos do projeto para o container
COPY . .

# Utiliza cache para acelerar o processo de build
RUN --mount=type=cache,target=/root/.m2,rw mvn clean package -DskipTests

# Etapa de execução
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o JAR gerado para o container
COPY --from=build /app/target/*.jar ./app.jar

# Configura o ponto de entrada para rodar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
