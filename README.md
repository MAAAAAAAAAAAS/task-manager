# Task Manager API

REST API для управления проектами и задачами. Разработан на Spring Boot.

## Технологии
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA (Hibernate)
- PostgreSQL
- Spring Security (JWT, роли USER/ADMIN)
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
- Глобальная обработка ошибок (400, 401, 403, 404).
- Аудит (дата создания и обновления автоматически).
- JWT-аутентификация и авторизация по ролям.

## Запуск через Docker Compose

1. Убедитесь, что установлен Docker Desktop.
2. Выполните в корне проекта:
   docker-compose up -d --build
3. Приложение будет доступно по адресу: http://localhost:8080
4. Swagger UI: http://localhost:8080/swagger-ui/index.html

Остановить:
docker-compose down

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
5. Swagger UI: http://localhost:8080/swagger-ui/index.html

## Безопасность (JWT)

Для доступа к API используется JSON Web Token (JWT). Приложение работает в режиме Stateless (без сессий). Каждый защищенный запрос должен содержать заголовок `Authorization: Bearer <токен>`.

Тестовые пользователи:
| Логин | Пароль   | Роль  | Права                          |
|-------|----------|-------|--------------------------------|
| user  | user123  | USER  | Просмотр данных (GET)          |
| admin | admin123 | ADMIN | Создание, обновление, удаление |

Как авторизоваться в Swagger UI:
1. Выполните запрос `POST /api/auth/login`, передав логин и пароль (например, `admin` / `admin123`).
2. Скопируйте полученный `token` из ответа.
3. Нажмите кнопку Authorize в правом верхнем углу Swagger UI.
4. Вставьте токен (без слова "Bearer") и нажмите Authorize.

## Endpoints

### Аутентификация
- `POST /api/auth/login` — получить JWT-токен (открыт для всех)

### Проекты
- `GET /api/projects` — список проектов (USER, ADMIN)
- `GET /api/projects/{id}` — проект по id (USER, ADMIN)
- `POST /api/projects` — создать (ADMIN)
- `PUT /api/projects/{id}` — обновить (ADMIN)
- `DELETE /api/projects/{id}` — удалить (ADMIN)
- `GET /api/projects/search/{part}` — поиск по названию (USER, ADMIN)

### Задачи
- `GET /api/tasks` — список задач (USER, ADMIN)
- `GET /api/tasks/{id}` — задача по id (USER, ADMIN)
- `POST /api/tasks` — создать (ADMIN)
- `PUT /api/tasks/{id}` — обновить (ADMIN)
- `DELETE /api/tasks/{id}` — удалить (ADMIN)
- `GET /api/tasks/priority/{priority}` — задачи по приоритету (USER, ADMIN)
- `GET /api/tasks/by-project/{projectId}` — задачи проекта (USER, ADMIN)
- `GET /api/tasks/deadline-before/{date}` — задачи с дедлайном до даты (USER, ADMIN)

## Тестирование

Проект покрыт интеграционными тестами (JUnit 5 + MockMvc) с использованием H2 (in-memory) базы для изоляции тестов.

./mvnw test

## Live Demo
**[Открыть Swagger UI](https://task-manager-db-9831.onrender.com/swagger-ui/index.html)**
*(Для входа используйте `POST /api/auth/login` с логином `admin` и паролем `admin123`, затем скопируйте токен в кнопку Authorize)*