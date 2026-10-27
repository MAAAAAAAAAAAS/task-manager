# Task Manager API

REST API для управления проектами и задачами. Разработан на Spring Boot.

## Технологии
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA (Hibernate)
- PostgreSQL
- Spring Security (Basic Auth, роли USER/ADMIN)
- Swagger / OpenAPI (автодокументация)
- JUnit 5 + MockMvc (тестирование)
- Maven (сборка)
- Docker / Docker Compose

## Функционал
- CRUD для проектов (создание, чтение, обновление, удаление).
- CRUD для задач.
- Связи `@OneToMany` / `@ManyToOne` между проектами и задачами.
- Поиск задач по приоритету, статусу (выполнено / не выполнено), дедлайну.
- Валидация входящих данных (`@NotBlank`, `@Min`, `@Max`).
- Глобальная обработка ошибок (404, 400).
- Аудит (дата создания и обновления автоматически).

## Запуск через Docker Compose

1. Убедитесь, что установлен Docker Desktop.
2. Выполните в корне проекта: docker-compose up -d --build
3. Приложение будет доступно по адресу: http://localhost:8080
4. Swagger UI: http://localhost:8080/swagger-ui/index.html
5. Логин/пароль: admin / admin123 (или user / user123)

Остановить: docker-compose down

## Локальный запуск (без Docker)

1. Установите Java 21 и PostgreSQL.
2. Создайте базу данных task_db (или используйте PostgreSQL в Docker — см. выше).
3. Настройте переменные окружения (или отредактируйте application.properties):
   SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/task_db
   SPRING_DATASOURCE_USERNAME=user
   SPRING_DATASOURCE_PASSWORD=password
4. Склонируйте репозиторий и запустите: 
   git clone https://github.com/MAAAAAAAAAAAS/task-manager.git
   cd task-manager
   ./mvnw spring-boot:run
5.
5. Swagger UI: http://localhost:8080/swagger-ui/index.html

## Безопасность

Для доступа к API используется Basic Auth.

| Логин | Пароль   | Роль  | Права                          |
|-------|----------|-------|--------------------------------|
| user  | user123  | USER  | Просмотр данных (GET)          |
| admin | admin123 | ADMIN | Создание, обновление, удаление |

Авторизация в Swagger UI: нажмите кнопку Authorize и введите логин/пароль.

## Endpoints

### Проекты
- `GET /api/projects` — список проектов
- `GET /api/projects/{id}` — проект по id
- `POST /api/projects` — создать (ADMIN)
- `PUT /api/projects/{id}` — обновить (ADMIN)
- `DELETE /api/projects/{id}` — удалить (ADMIN)
- `GET /api/projects/search/{part}` — поиск по названию

### Задачи
- `GET /api/tasks` — список задач
- `GET /api/tasks/{id}` — задача по id
- `POST /api/tasks` — создать (ADMIN)
- `PUT /api/tasks/{id}` — обновить (ADMIN)
- `DELETE /api/tasks/{id}` — удалить (ADMIN)
- `GET /api/tasks/priority/{priority}` — задачи по приоритету
- `GET /api/tasks/by-project/{projectId}` — задачи проекта
- `GET /api/tasks/deadline-before/{date}` — задачи с дедлайном до даты

## Тестирование

Проект покрыт интеграционными тестами (JUnit 5 + MockMvc).
./mvnw test