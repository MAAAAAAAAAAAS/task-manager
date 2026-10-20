# Task Manager API

REST API для управления проектами и задачами. Разработан на Spring Boot.

## Технологии
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA (Hibernate)
- H2 Database (в памяти)
- Spring Security** (Basic Auth, роли USER/ADMIN)
- Swagger / OpenAPI (автодокументация)
- JUnit 5 + MockMvc (тестирование)
- Maven (сборка)

## Функционал
- CRUD для проектов (создание, чтение, обновление, удаление).
- CRUD для задач.
- Связи OneToMany, ManyToOne
- Поиск задач по приоритету, статусу (выполнено/не выполнено), дедлайну.
- Валидация входящих данных (`@NotBlank`, `@Min`, `@Max`).
- Глобальная обработка ошибок (404, 400).
- Аудит (дата создания и обновления автоматически).

## Как запустить
1. Убедитесь, что у вас установлена Java 21.
2. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/ВАШ_НИК/task-manager.git

## Доступно по адресу http://localhost:8080

## Безопасность
- Для доступа к API используется Basic Auth.

- USER (роль USER): может просматривать данные (GET).
- ADMIN (роль ADMIN): может создавать, обновлять и удалять данные (POST, PUT, DELETE).

    Пользователь
- Логин: user	
- Пароль: user123
- Роль: USER

    Админ
- Логин: admin
- Пароль: admin123
- Роль: ADMIN
  