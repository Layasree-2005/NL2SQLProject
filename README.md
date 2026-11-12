
# NL2SQL – Natural Language to SQL Application

## 1. Project Overview

NL2SQL is a web-based application that converts natural-language questions into SQL queries using Google's Gemini API.

Users can enter questions in plain English, view the generated SQL, edit the query, execute it against a MySQL database, and view the results in a structured table.

The application also maintains query history so users can revisit previously executed queries.

## 2. Features

- Natural-language to SQL generation using Gemini API.
- SQL query preview before execution.
- Manual SQL editing.
- Execution of SELECT queries against MySQL.
- Dynamic result tables.
- Support for JOIN queries across related tables.
- Query history with execution status.
- Restore previous questions and SQL queries.
- Input validation and error handling.
- Protection against database modification queries.
- Handling of invalid and out-of-scope questions.

## 3. Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming |
| Spring Boot 3.5.6 | Backend application framework |
| Spring JDBC / JdbcTemplate | Database connectivity and query execution |
| MySQL | Relational database |
| Google Gemini API | Natural-language SQL generation |
| HTML5 | Frontend structure |
| CSS3 | Frontend styling |
| JavaScript | Frontend interactions and API communication |
| Maven | Dependency management and build |
| Eclipse IDE | Development environment |
| Git and GitHub | Version control and source-code hosting |

## 4. Database Schema

The application uses a MySQL database named `n12sql_db`.

It contains three related tables:

### Departments
- department_id – Primary key
- department_name – Department name

### Employees
- employee_id – Primary key
- employee_name – Employee name
- email – Employee email
- department_id – Foreign key referencing departments
- hire_date – Employee joining date

### Attendance
- attendance_id – Primary key
- employee_id – Foreign key referencing employees
- attendance_date – Attendance date
- status – Attendance status

### Relationships

- One department can have multiple employees.
- Each employee belongs to a department.
- One employee can have multiple attendance records.

Each table contains at least 50 records in the current test database.

## 5. Database Setup

1. Install MySQL Server.
2. Create the database:

```sql
CREATE DATABASE n12sql_db;
```

3. Create the departments, employees, and attendance tables using the project database SQL script.
4. Insert at least 50 records into each table.
5. Configure a database user with SELECT permissions and the required INSERT permission for query history.

## 6. Application Configuration

Configure the following environment variables before running the application:

- `DB_USERNAME` – MySQL username
- `DB_PASSWORD` – MySQL password
- `GEMINI_API_KEY` – Google Gemini API key

Configure the database URL in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/n12sql_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

server.port=8081

gemini.api.key=${GEMINI_API_KEY}
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent
```

Never commit actual API keys or passwords to GitHub.

## 7. Steps to Run Locally

1. Clone the GitHub repository.
2. Import the project into Eclipse as an existing Maven project.
3. Configure MySQL and create the database.
4. Execute the database schema and sample-data SQL scripts.
5. Configure the required environment variables.
6. Update Maven dependencies.
7. Run the Spring Boot main application class.
8. Open the application in a browser:

`http://localhost:8081`

## 8. Assumptions and Trade-offs

- The application supports questions related to employees, departments, and attendance.
- Only SELECT queries are allowed for database retrieval.
- Database modification operations are restricted.
- Gemini API availability and response quality affect SQL generation.
- Generated SQL is validated before execution.
- The application uses a predefined database schema rather than dynamically discovering arbitrary database schemas.
- A dedicated database user with restricted permissions provides an additional security layer.

## 9. Future Enhancements

- Support additional database schemas.
- Improve SQL validation using a SQL parser.
- Add advanced result pagination.
- Improve query generation accuracy.
- Add user authentication and role-based access.

## 10. Author

Developed as a Natural Language to SQL project using Java, Spring Boot, MySQL, and Gemini API.
