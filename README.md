# REST Assured Advanced Framework

A production-ready API testing framework with advanced **contract testing** capabilities using REST Assured, TestNG, and JSON Schema validation.

## Project Structure

```
rest-assured-framework/
├── pom.xml                                    # Maven dependencies
├── testng.xml                                 # TestNG suite configuration
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/framework/
│   │   │   ├── base/
│   │   │   │   └── BaseTest.java              # Base test class with global setup
│   │   │   ├── config/
│   │   │   │   ├── ConfigFactory.java         # Config interface (Owner library)
│   │   │   │   └── ConfigManager.java         # Singleton config manager
│   │   │   ├── endpoints/
│   │   │   │   └── UserEndpoints.java         # API endpoint methods
│   │   │   ├── models/
│   │   │   │   ├── request/
│   │   │   │   │   ├── CreateUserRequest.java # Request POJOs
│   │   │   │   │   └── LoginRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── UserResponse.java      # Response POJOs
│   │   │   │       ├── CreateUserResponse.java
│   │   │   │       └── ErrorResponse.java
│   │   │   ├── specs/
│   │   │   │   └── SpecBuilder.java           # Reusable request/response specs
│   │   │   └── utils/
│   │   │       ├── SchemaValidator.java       # JSON Schema validation utility
│   │   │       ├── ContractValidator.java     # Advanced contract validation (fluent API)
│   │   │       └── ResponseHelper.java        # Response extraction helpers
│   │   └── resources/
│   │       ├── config.properties              # Framework configuration
│   │       └── log4j2.xml                     # Logging configuration
│   └── test/
│       ├── java/com/framework/tests/
│       │   └── contract/
│       │       ├── UserSchemaContractTest.java     # Schema-based contract tests
│       │       └── UserAdvancedContractTest.java   # Advanced contract tests
│       └── resources/
│           └── schemas/
│               ├── get-user-schema.json            # JSON Schema contracts
│               ├── get-users-list-schema.json
│               ├── create-user-schema.json
│               └── update-user-schema.json
```

## Key Features

### Contract Testing (Two Approaches)

#### 1. JSON Schema Validation (`SchemaValidator`)
Validates API responses against predefined JSON schema files:
```java
response.then()
    .body(SchemaValidator.matchesSchema("get-user-schema.json"));
```

#### 2. Fluent Contract Validation (`ContractValidator`)
Programmatic contract checks with detailed violation reporting:
```java
ContractValidator.forResponse(response)
    .expectStatusCode(200)
    .expectContentType(ContentType.JSON)
    .expectResponseTimeBelow(5000)
    .expectFieldPresent("data.id")
    .expectFieldType("data.id", Integer.class)
    .expectFieldValue("page", 1)
    .expectListMinSize("data", 1)
    .expectListItemsHaveFields("data", "id", "email", "first_name")
    .expectSchema("get-users-list-schema.json")
    .assertContract();
```

### Other Features
- **Config Management** - Owner library for type-safe configuration
- **Spec Builder** - Reusable request/response specifications
- **POJO Models** - Lombok-powered request/response models with Jackson serialization
- **Allure Reporting** - Built-in Allure integration for rich test reports
- **Logging** - Log4j2 with console + file appenders
- **Parallel Execution** - TestNG parallel class execution

## Prerequisites
- Java 17+
- Maven 3.8+

## How to Run

### Run all tests:
```bash
mvn clean test
```

### Run only contract tests:
```bash
mvn clean test -Dtest="com.framework.tests.contract.*"
```

### Run with a different environment:
```bash
mvn clean test -Dbase.url=https://staging-api.example.com
```

### Generate Allure report:
```bash
mvn allure:serve
```

## Adding New Contract Tests

1. **Define the JSON Schema** in `src/test/resources/schemas/`
2. **Create endpoint methods** in a new `*Endpoints.java` class
3. **Write test class** extending `BaseTest` in `src/test/java/.../contract/`
4. **Add to `testng.xml`** for suite execution

## API Under Test
This framework is configured to test [ReqRes API](https://reqres.in) — a free hosted REST API for testing.
