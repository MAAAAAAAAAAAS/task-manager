# ЭТАП 1: Сборка проекта (Build)
# Используем образ с JDK 21 для компиляции
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Копируем файлы Maven Wrapper и pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Даем права на запуск mvnw и скачиваем зависимости
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Копируем исходный код и собираем JAR (пропуская тесты для скорости)
COPY src src
RUN ./mvnw package -DskipTests

# ЭТАП 2: Запуск (Run)
# Используем легкий образ только с JRE (без JDK), чтобы образ был меньше
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Копируем собранный JAR из первого этапа
COPY --from=build /app/target/*.jar app.jar

# Открываем порт 8080 (для документации)
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]