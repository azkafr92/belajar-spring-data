# Validation

## Validating the Schema

Validation is the process of ensuring that data is correct and meets certain criteria. In a Spring Data application, validation is used to ensure that data is valid before it is persisted. For example, you might want to ensure that a user's email address is a valid email address before storing it in a database.

A good practice is to use the validation in the first layer, a microservice that uses Spring Boot as the controller
but is not exclusively. You can use it in another layer, like the service or repositories, if you have an implementation.

## Dependency

Add `spring-boot-starter-validation` to `pom.xml`.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## Applying Validation

Add annotation to `RegisterUserDTO`, this is used in controller layer as form to add new user. Here, we add `@Email`, `@NotBlank`, and `@Pattern`.

`dtos/requests/RegisterUserDTO.java`

```java
// omitted code
public class RegisterUserDTO implements Serializable {
    // omitted code
    @Email
    private String email;

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "[a-zA-Z0-9_]*", message = "username can only contains alphabet, number, and underscore")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$",
            message = "password must have at least one digit, one lowercase, one uppercase, one special character, and minimum 8 characters")
    private String password;
    // omitted code
}
```

Create `dtos/responses/ViolationDTO.java` and `dtos/responses/ValidationErrorDTO.java` as response for invalid request.

`dtos/responses/ViolationDTO.java`

```java
public class ViolationDTO implements Serializable {
   private String field;
   private String message;
   public ViolationDTO(String field, String message) {
       this.field = field;
       this.message = message;
   }
   public ViolationDTO(String message) {
       this.message = message;
   }
}
```

`dtos/responses/ValidationErrorDTO.java`

```java
public class ValidationErrorDTO extends ResponseDTO {
   private List<ViolationDTO> violations;
   public ValidationErrorDTO()  {
       super(HttpStatus.BAD_REQUEST.value(), "failed");
       this.violations = new ArrayList<>();
   }
}
```

Create custom handler for `ConstraintViolationException` in `exceptions/ErrorHandlingControllerAdvice.java`, create method `onConstraintValidationException`.

```java
@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO onConstraintValidationException(ConstraintViolationException e) {
        ValidationErrorDTO errors = new ValidationErrorDTO();
        errors.setViolations(
                e.getConstraintViolations()
                        .stream()
                        .map(violation -> new ViolationDTO(violation.getPropertyPath().toString(), violation.getMessage()))
                        .collect(Collectors.toList())
        );
        return errors;
    }
}
```

## Validating Unique Field

For example, we want to ensure no user with same email or username registered before writing to database.

First create custom handler for `DataIntegrityViolationException` in class `ErrorHandlingControllerAdvice`. Then, create method `onDataIntegrityViolationException`.

`exceptions/ErrorHandlingControllerAdvice.java`

```java
@ControllerAdvice
public class ErrorHandlingControllerAdvice {
   // omitted code
   @ExceptionHandler(DataIntegrityViolationException.class)
   @ResponseStatus(HttpStatus.CONFLICT)
   @ResponseBody
   public ValidationErrorDTO onDataIntegrityViolationException(DataIntegrityViolationException e) {
      ValidationErrorDTO errors = new ValidationErrorDTO();
      ViolationDTO violationDTO = new ViolationDTO(e.getMessage());
      errors.getViolations().add(violationDTO);
      return errors;
   }
}
```

In service layer (`UserService`), throw `DataIntegrityViolationException` if user with same email or username found.

`services/UserService.java`

```java
@Service
public class UserService implements IUserService {
   @Override
   public void addUser(RegisterUserDTO userRequest) {
      if (userRepository.existsByEmailOrUsername(userRequest.getEmail(), userRequest.getUsername())) {
          throw new DataIntegrityViolationException("email or username already used");
      }

      User user = modelMapper.map(userRequest, User.class);
      user.setAudit(new Audit());
      userRepository.save(user);
   }
}
```

In repository layer (`UserRepository`), create method `existByEmailOrUsername`.

`repositories/UserRepository.java`

```java
public interface IUserRepository extends JpaRepository<User, String> {
   boolean existsByEmailOrUsername(String email, String username);
}
```


*Annotations to Validate Attribute*

| Name                          | Description                                                         |
|-------------------------------|---------------------------------------------------------------------|
| @NotBlank                     | Validates that the string is not null or with whitespaces           |
| @NotNull                      | Checks if the property is not null                                  |
| @Min                          | Checks if the number is greater or equal to the value               |
| @Max                          | Checks if the number is smaller or equal to the value               |
| @AssertTrue                   | Validates if the value of the attribute is true or not              |
| @Past and @PastOrPresent      | Validate if the date is in the past or not the present              |
| @Future and @FutureOrPresent  | Validate if the date is in the future, including or not the present |
| @Positive and @PositiveOrZero | Validate if the number is positive, including or not the zero       |
| @Negative and @NegativeOrZero | Validate if the number is negative, including or not the zero       |
| @Pattern                      | Ensures that a field matches a specified regular expression pattern |
