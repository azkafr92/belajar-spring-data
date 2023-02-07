# Introduction to Spring Data

Spring Data dynamically creates a lot of classes in runtime to transform the interface into classes.
Think of this approach as a proxy, using the interface instead of the classes that access the database.

## Core Concept

This section will be introducing core concepts of Spring Data, starting from:

- [Object Mapping](#object-mapping)
- [Repositories](#repositories)
- [Automatic Custom Queries](#automatic-custom-queries)
- [Manual Custom Queries](#manual-custom-queries)

### Object Mapping

[//]: # (TODO: penjelasan mengenai Object Mapping)

### Repositories

[//]: # (TODO: penjelasan mengenai Repositories)

### Automatic Custom Queries

*Example*

```java
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailOrUsername();
}
```

*Supported Query Method Subject Keywords*

| Keyword                               | Description                                                                                                                      |
|---------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| findBy...<br/>getBy...<br/>queryBy... | generally associated with a select query and return an element or set of elements that can be a Collection or Streamable subtype |
| countBy                               | returns the number of elements that match the query                                                                              |
| existBy                               | returns the number of elements that match the query                                                                              |
| deleteBy                              | removes a set of elements that matches the query but does not return anything                                                    |

*Supported Query Method Predicate Keywords*

| Logical Keyword | Keyword Expression |
|-----------------|--------------------|
| AND             | And                |
| OR              | Or                 |

See link below to read more details:
- [Supported Query Method Subject Keywords](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#appendix.query.method.predicate)
- [Supported Query Method Predicate Keywords](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#appendix.query.method.predicate)

### Manual Custom Queries

Why need manual query? The answers could be:
- Improve the performance of the query that Spring Data generates
- You don’t need all the attributes of the table
- The query is so complex that not exist keyword to express it

*Example*

```java
public interface IUserRepository extends JpaRepository<User, String> {
  @Query("SELECT email e, username un FROM User u WHERE e = :emailOrUsername OR un = :emailOrUsername")
  List<GetUsersListResponse> findByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);
}
```

Notice that manual query using **class name** instead of table name. From the example above, it is `...FROM User u...`.

> Read
> [Spring Data JPA Query](https://www.baeldung.com/spring-data-jpa-query),
> [Spring Data Criteria Queries](https://www.baeldung.com/spring-data-criteria-queries)
> for more detail explanation.
