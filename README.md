# Extended Problem Detail

A Spring Boot Starter that provides enhanced ProblemDetail exception handling with field-level error information, following [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) (Problem Details for HTTP APIs).

## Features

- Extends Spring Framework's `ProblemDetail` with detailed field-level error information
- Automatic exception handling for validation errors
- Support for both Spring WebMVC and Spring WebFlux
- Configurable logging levels and stack trace printing
- Easy integration with Spring Boot auto-configuration

## Modules

| Module | Description |
|--------|-------------|
| `core` | Core module containing response classes and validation error handler |
| `autoconfigure-webmvc` | Auto-configuration for Spring WebMVC applications |
| `autoconfigure-webflux` | Auto-configuration for Spring WebFlux applications |
| `spring-boot-starter-webmvc` | Starter for WebMVC applications (includes all dependencies) |
| `spring-boot-starter-webflux` | Starter for WebFlux applications (includes all dependencies) |

## Installation

### WebMVC Application

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.sbracely</groupId>
    <artifactId>extended-problem-detail-spring-boot-starter-webmvc</artifactId>
    <version>1.0.0</version>
</dependency>
```

### WebFlux Application

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.sbracely</groupId>
    <artifactId>extended-problem-detail-spring-boot-starter-webflux</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Configuration

### Application Properties

```yaml
extended:
  problem-detail:
    enabled: true              # Enable/disable the feature (default: true)
    log-level: DEBUG           # Log level for validation exceptions (default: DEBUG)
    print-stack-trace: false   # Print exception stack trace (default: false)
```

Or using `application.properties`:

```properties
extended.problem-detail.enabled=true
extended.problem-detail.log-level=DEBUG
extended.problem-detail.print-stack-trace=false
```

## Usage

Once the starter is added to your project, it automatically handles validation exceptions and returns extended problem detail responses.

### Example Response

When a validation error occurs, the response will include detailed error information:

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid request content.",
  "instance": "/api/users",
  "errors": [
    {
      "type": "PARAMETER",
      "target": "email",
      "message": "must be a well-formed email address"
    },
    {
      "type": "PARAMETER",
      "target": "password",
      "message": "size must be between 8 and 20"
    }
  ]
}
```

### Error Types

The `errors` array contains objects with the following properties:

| Field | Description |
|-------|-------------|
| `type` | Error source type: `PARAMETER`, `COOKIE`, `HEADER`, or `BUSINESS` |
| `target` | The field name or resource that caused the error |
| `message` | Human-readable error message |

### Supported Exceptions

The following exceptions are automatically handled:

- `MethodArgumentNotValidException` - Validation failures for `@Valid` annotated arguments
- `HandlerMethodValidationException` - Method parameter validation failures
- `WebExchangeBindException` - Data binding exceptions
- `MethodValidationException` - Method-level validation errors

## Requirements

- Java 17+
- Spring Boot 4.0.5+

## License

This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Author

- **sbrace** - [sbrace0822@gmail.com](mailto:sbrace0822@gmail.com)
