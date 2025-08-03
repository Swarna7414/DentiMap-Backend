# Database Setup Guide

## MySQL Configuration for Spring Boot Security Application

### Database Schema
The application uses the following user table structure:
- `id` (Primary Key)
- `first_name` (Required)
- `last_name` (Required)
- `date_of_birth` (Required, Past date)
- `gender` (Required)
- `email` (Required, Unique)
- `password` (Required, Encrypted)
- `created_at` (Auto-generated)
- `email_verified` (Boolean, Default: false)
- `email_verified_at` (DateTime, Nullable)

### Step 1: Ensure MySQL is Running
- Make sure MySQL Server is running on your system
- Default port: 3306

### Step 2: Create the Database

**Option A: Using MySQL Command Line**
```bash
# Connect to MySQL (you'll be prompted for password)
mysql -u root -p

# Once connected, run:
CREATE DATABASE harshithsecurity;
```

**Option B: Using the provided SQL script**
```bash
# Run the setup script
mysql -u root -p < setup-database.sql
```

### Step 3: Update Application Properties

Edit `src/main/resources/application.properties` and update the MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/harshithsecurity
spring.datasource.username=root
spring.datasource.password=Harihara01@01
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
server.servlet.session.timeout=0
```

**Database password is configured as: `Harihara01@01`**

### Step 4: Test Database Connection

After updating the password, run:
```bash
mvn spring-boot:run
```

### Common Issues:

1. **"Access denied for user 'root'@'localhost'"**
   - Solution: Update the password in application.properties
   - Or create a new MySQL user with proper permissions

2. **"Unknown database 'harshithsecurity'"**
   - Solution: Create the database using the steps above

3. **MySQL not running**
   - Start MySQL service from Windows Services or MySQL Workbench

### Alternative: Use H2 Database (for testing)

If you want to test without MySQL, you can temporarily switch to H2 in-memory database by updating application.properties:

```properties
# H2 Database Configuration (for testing)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

Then add H2 dependency to pom.xml:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
``` 