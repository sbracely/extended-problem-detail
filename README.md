# Extended Problem Detail

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen.svg)](https://spring.io/projects/spring-boot)

## Project Overview

Extended Problem Detail is a Spring Boot Starter library that extends Spring Framework 6's `ProblemDetail` functionality. It provides richer error details, especially when handling parameter validation errors, returning detailed field-level error information.

## Features

- ✅ Extends `ProblemDetail` with custom error list support
- ✅ Automatically handles `HandlerMethodValidationException` parameter validation exceptions
- ✅ Automatically handles `MethodArgumentNotValidException` method argument validation exceptions
- ✅ Supports both Spring WebMVC and Spring WebFlux
- ✅ Fully automated configuration, ready to use out of the box
- ✅ Configurable enable/disable

## Module Structure

```
extended-problem-detail
├── response                          # Core response object module
│   └── ExtendedProblemDetail         # Extended ProblemDetail implementation
├── autoconfigure-webmvc              # WebMVC auto-configuration module
│   └── MvcExtendedProblemDetailAutoConfiguration
├── autoconfigure-webflux             # WebFlux auto-configuration module
│   └── FluxExtendedProblemDetailAutoConfiguration
├── starter-webmvc                    # WebMVC Starter aggregate package
└── starter-webflux                   # WebFlux Starter aggregate package
```

## Quick Start

### Maven Dependencies

#### WebMVC Project

```xml
<dependency>
    <groupId>com.github.sbracely</groupId>
    <artifactId>extended-problem-detail-spring-boot-starter-webmvc</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### WebFlux Project

```xml
<dependency>
    <groupId>com.github.sbracely</groupId>
    <artifactId>extended-problem-detail-spring-boot-starter-webflux</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Configuration

Configure in `application.yml` or `application.properties`:

```yaml
extended:
  problem-detail:
    enabled: true  # Enabled by default, optional
```

## Core Components

### 1. ExtendedProblemDetail

Extends Spring Framework's `ProblemDetail` class, adding an `errors` field for storing detailed error information lists.

```java
public class ExtendedProblemDetail extends ProblemDetail {
    @Nullable
    private List<Error> errors;
    // ... getters and setters
}
```

### 2. Error Object

Represents detailed information for a single error:

```java
public class Error {
    private Type type;      // Error type: PARAMETER, HEADER, COOKIE
    private String field;   // Field name
    private String message; // Error message
}
```

### 3. Exception Handlers

#### WebMVC: MvcExtendedProblemDetailExceptionHandler

- Inherits from `ResponseEntityExceptionHandler`
- Handles `MethodArgumentNotValidException`
- Handles `HandlerMethodValidationException` (using Visitor pattern)
- Handles `WebExchangeBindException`

#### WebFlux: FluxExtendedProblemDetailExceptionHandler

- Inherits from `reactive.result.method.annotation.ResponseEntityExceptionHandler`
- Handles `WebExchangeBindException`
- Handles `HandlerMethodValidationException` (using Visitor pattern)

## HandlerMethodValidationException Handling

Both exception handlers use the **Visitor pattern** to handle `HandlerMethodValidationException`, supporting the following annotation types:

- `@CookieValue`
- `@MatrixVariable`
- `@ModelAttribute`
- `@PathVariable`
- `@RequestBody`
- `@RequestHeader`
- `@RequestParam`
- `@RequestPart`
- Other unhandled validation results (logged)

## Examples

### Controller Example

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @PostMapping
    public ResponseEntity<User> createUser(
            @Valid @RequestBody UserCreateRequest request) {
        // ...
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable Long id,
            @RequestParam @NotBlank String name) {
        // ...
    }
}
```

### Request Validation Example

```java
public class UserCreateRequest {
    
    @NotBlank(message = "Username cannot be empty")
    private String username;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @CheckPassword(message = "Password strength insufficient")
    private String password;
    
    @ConfirmPassword(message = "Passwords do not match")
    private String confirmPassword;
    
    // getters and setters
}
```

### Response Example

When validation fails, the response format:

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for argument [0] in public ...",
  "instance": "/api/users",
  "errors": [
    {
      "type": "PARAMETER",
      "field": "username",
      "message": "Username cannot be empty"
    },
    {
      "type": "PARAMETER",
      "field": "email",
      "message": "Invalid email format"
    }
  ]
}
```

## Technology Stack

- **Java**: 17+
- **Spring Framework**: 6.x
- **Spring Boot**: 4.0.5
- **Validation**: Jakarta Validation (Bean Validation 3.0)
- **Reactive**: Project Reactor (WebFlux module)

## Development Notes

### Build Project

```bash
./mvnw clean install
```

### Run Tests

```bash
./mvnw test
```

## License

Apache License, Version 2.0

## Author

- sbrace (sbrace0822@gmail.com)

## GitHub

https://github.com/sbracely/extended-problem-detail
