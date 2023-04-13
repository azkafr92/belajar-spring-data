# Listening and Auditing Events

In Spring Data JPA, you can configure your entities to listen and respond to various events that occur during the lifecycle of an entity. These events include pre-persist, post-persist, pre-update, post-update, pre-remove, and post-remove events. You can use these events to perform various tasks, such as logging, validation, or auditing.

Auditing events allow you to keep track of changes to your entities, such as who created or updated an entity and when. To implement auditing, you can add additional columns to your entities to store the auditing information, such as the user who made the change and the date and time of the change.

> How to store users who are creating or updating data will be discussed in Spring Security

_Table 3.1. Events Available in the Life Cycle_

| Events                                                              | Description                                                                                                                                      |
|---------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| @jakarta.persistence.PrePersist<br>@jakarta.persistence.PostPersist | The event calls it after or before the entity has persisted.                                                                                     |
| @jakarta.persistence.PreUpdate<br>@jakarta.persistence.PostUpdate   | The event calls it after or before the entity has been updated.                                                                                  |
| @ jakarta.persistence.PreRemove<br>@jakarta.persistence.PostRemove  | The event calls it after or before the entity has been removed.                                                                                  |
| @ jakarta.persistence.PostLoad                                      | The event calls it after the entity has been loaded successfully.                                                                                |

`entities/Audit.java`

```java
@Embeddable
public class Audit {
    // omitted code
    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
    // omitted code
}
```

`entities/User.java`

```java
@Entity
@Table(name = "users")
public class User {
    // omitted code
    @Embedded
    private Audit audit;

    @PrePersist
    public void fillCreatedOn() {
        audit.setCreatedOn(LocalDateTime.now());
    }

    @PreUpdate
    public void fillUpdatedOn() {
        audit.setUpdatedOn(LocalDateTime.now());
    }
    // omitted code
}
```

- `User` has an embedded object `Audit`, annotated with `@Embedded` which contains the audit information.
- `Audit` is annotated with `@Embeddable`. It is embedded in the `User`. It contains two fields, `createdOn` and `updatedOn`, which represent the timestamps for when the entity was created and last updated, respectively.
- `User` class also contains two methods annotated with `@PrePersist` and `@PreUpdate`. These methods  invoked before `User` is persisted or updated in the database.
- Every time a new `User` entity is persisted or an existing one is updated, the `Audit` object within the `User` entity will be updated with the current timestamp, allowing you to keep track of when the entity was created or last updated.

In the previous section, you added attributes to the entities to audit the rows in the database. But you need to manually set the value of these attributes, which is not the best approach and implies that you repeat the logic in all the services.

JPA offers a set of annotations related to the events to check what happens in the life cycle of an entity and introduce logs or modifications.

_Table 3.2. Events Available in the Spring Data Life Cycle_

| Events                                                              | Description                                                                                                                                      |
|---------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| @CreatedDate<br>@LastModifiedDate                                   | These annotations are equivalent to @PreUpdate, @PrePersist                                                                                      |
| @CreatedBy<br>@LastModifiedBy                                       | These annotations are responsible for doing the modifications in the entity.<br>In most cases, the annotations are used over a String attribute. |

```java
@SpringBootApplication
@EnableJpaAuditing
public class BelajarspringdataApplication {
	public static void main(String[] args) {
		SpringApplication.run(BelajarspringdataApplication.class, args);
	}
}
```

```java
@Embeddable
public class Audit {
    // omitted code
    @Column(name = "created_on", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    @LastModifiedDate
    private LocalDateTime updatedOn;
    // omitted code
}
```

```java
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    // omitted code
    @Embedded
    private Audit audit;
    // omitted code
}
```

You can also externalize all the logic of the persistence life cycle in one class and annotate the classes with `@EntityListeners(AuditingEntityListener.class)`, and include the `@EnableJpaAuditing` annotation in your main class. JPA provides `@EntityListeners` to specify callback listener classes. Spring Data provides its own JPA entity listener class, `AuditingEntityListener`.

> Further read: https://www.baeldung.com/database-auditing-jpa