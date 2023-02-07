# Validating the Schema

A good practice is to use the validation in the first layer, a microservice that uses Spring Boot as the controller
but is not exclusively. You can use it in another layer, like the service or repositories, if you have an implementation.

Add `spring-boot-starter-validation` to `pom.xml`.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

```java
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService implements IUserService {
    // omitted code
    @Autowired
    Validator validator;
    
    public User registerUser(UserDTO userDTO) {
        // omitted code
        Set<ConstraintViolation<Currency>> violations = validator.validate(entity);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        // omitted code
    }
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
