# Entities

This section will be explaining entity and annotations that build entity.

- [What is entity?](#what-is-entity)
- [Declaring An Entity](#declaring-an-entity)
- [Column](#column)
  - [Primary Key and Generator (`@Id` and `@GeneratedValue`)](#primary-key-and-generator-id-and-generatedvalue)
  - [`@Column`](#column)
  - [`@Enumerated`](#enumerated)
  - [Equivalence Between the SQL Type and Java Type](#equivalence-between-the-sql-type-and-java-type)
- [Constructor](#constructor)
- [Getter and Setter](#getter--setter)
- [`toString()`, `equals()`, `hashCode()`](#tostring-equals-hashcode)
- [Types of Inherence](#types-of-inherence)
  - [Mapped Superclass](#mapped-superclass)
- [Embeddable Class](#embeddable-class)

## What is entity?

`Entity` is a class whose purpose is to store/retrieve data to/from a data store. It is represents a table in a relational database. Example:

`entities/User.java`

```java
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(unique = true, nullable = false)
  @NotBlank(message = "Email is mandatory")
  private String email;

  @Column(unique = true, nullable = false, length = 32)
  @NotBlank(message = "Username is mandatory")
  private String username;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Address> addresses;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "user_tags",
    joinColumns = {@JoinColumn(name = "user_id")},
    inverseJoinColumns = {@JoinColumn(name = "tag_id")}
  )
  private Set<Tag> tags;

  @Enumerated(EnumType.STRING)
  private Set<Authority> authorities;

  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private UserIdentity userIdentity;

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
  // constructors, getters, setters, toString, equals, & hashCode
}
```

## Declaring an Entity

JPA offers a simple way to translate a Java class into a table in the database using different types of annotations. The most important are `@Entity` and `@Table` because both help JPA understand all the attributes inside the class that need to be persisted in a table.

`@Entity` is to make the class an entity that will be recognized by Spring JPA. [Documentation](https://jakarta.ee/specifications/persistence/3.1/apidocs/jakarta.persistence/jakarta/persistence/entity).

`@Table` is an optional annotation to override default table name and schema. [Documentation](https://jakarta.ee/specifications/persistence/3.1/apidocs/jakarta.persistence/jakarta/persistence/table).

Rules to follow to be considered valid entity:

- All the attributes need to have a `getter` and `setter`
- Each entity needs to have an attribute with `@Id` annotation to indicate the primary key or the main attribute for search
- The classes must not be declared `final`
- Optional, but a good practice is to include overriding the `hashCode` and `equals` methods
- Entity must have a no-argument constructor method that is either public or protected that cannot be defined. JPA uses it as default constructor.

We also have a constructor where the id field isn’t provided: a constructor designed for creating new entries in the database.
When the id field is null, it tells JPA we want to create a new row in the table.

## Column

After you declare the table’s name in your class, the next step is to declare the name and the type of each column that matches each class attribute. By default, each of class variable/attribute represent a column in the table.

> Using `Spring Boot 3`, make sure the annotation is imported from `jakarta.persistence.*`. See the documentation [here](https://jakarta.ee/specifications/persistence/3.1/apidocs/jakarta.persistence/jakarta/persistence/package-summary.html).

### Primary Key and Generator (`@Id` and `@GeneratedValue`)

`@Id` set the attribute as primary key. JPA recognize this property as object's ID. [Documentation](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1.html#a14827).

Sometimes, the best option is to use a `Long` key because you save a short number of rows in the database. On the other hand, you can have an entity with a huge number of rows, so a good option could use a `UUID`. Also, another reason to use a `UUID` is for security because if your application exposes an endpoint that gives all the information of an entity using the `ID`, you can increment or decrement the `ID` and obtain the rows of a table instead if you use a `UUID` reduces the risk that someone knows which are valid UUIDs that exist in the database.

`entities/User.java`

```java
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;


  // omitted code
}
```

After primary key selected, the next thing to do is to define strategy to generate the value. `@GeneratedValue` to make id auto generated by whatever strategy we use. ID will be generated automatically. [Documentation](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1.html#a14790).
- `GenerationType.SEQUENCE` defines a numeric sequence in the database, so before persisting the information in the JPA table, call the sequence to obtain the next number to insert into the table. The main benefit of using the sequence is that you can use it in any column in multiple tables connected directly by one table, but it’s a common practice to use it for a specific purpose. Some databases that support the use of SEQUENCE are Oracle and PostgreSQL.
- `GenerationType.IDENTITY` is a special behind-the-scenes column that does the same as the SEQUENCE check, which is the next available value. Some databases do not support the definition of a SEQUENCE, so they have an alternative special column like this that is an auto-incremented value.
- `GenerationType.TABLE` is an alternative approach when you have a database that does not support using SEQUENCE; for example, MySQL 5.7 and lower do not have it. The goal is to have a table in your schema containing one row per entity that needs to generate an ID, which is the next available value.
- `GenerationType.AUTO` is a strategy that considers the database you used and defines which is the best option to use. You can indicate this strategy in the annotation or without anything `@GeneratedValue()` because both cases indicate the same.

### `@Column`

`entities/User.java`

```java
@Entity
@Table(name = "users")
public class User {
  // omitted code

  @Column(unique = true, nullable = false, length = 255)
  private String email;

  @Enumerated(EnumType.STRING)
  private Set<Authority> authorities;

  // omitted code
}

public enum Authority {
   READ, WRITE, UPDATE, DELETE
}
```

By default, each of class variable/attribute represent a column in the table. We can override the default setup, like name, length, minimum, or maximum by using `@Column` annotation. So, is it necessary to include the `@Column` annotation in all the attributes?

 It is not necessary to include `@Column` annotation because JPA supposes that the name of the attribute and the column in the database are the same. Although, good practice is defined in all cases, including the annotation and defining the name if it allows null values, and all the possible things you can use to validate the entity before persisting it (name, length, maximum, and minimum, whether it supports null values or not). Also, consider adding the String columns to the correct length of the attributes.  Some vendors of relational databases differentiate between words in lowercase and uppercase, so a good practice is to try to indicate the name of the columns in all cases.

`@Column` properties:

- `name` : override the name of the column in the database table. By default, the column name is the same as the field name
- `nullable` : is column can/cannot contain null values. The default value is `true`
- `insertable` : is column can be included in an insert operation. The default value is `true`
- `updateable` : is column can be included in an update operation. The default value is `true`
- `unique` : is column value must be unique. The default value is `false`
- `length` : specifies the length of the column for string fields
- `precision` : specifies the precision of the column for numeric fields
- `scale` : specifies the scale of the column for numeric fields

### `@Enumerated`

We are also using `@Enumerated` annotation to define column. You can save the enumeration as a string or an ordinal type like a number and delegate to the framework the responsibility to transform a column’s information into a value of the enumeration.

When an enum type field is mapped to a database column using the `@Enumerated` annotation, Spring Data JPA will convert the enum type value to the corresponding database column value and vice versa.

The `@Enumerated` annotation has properties `value` that specifies how the enum type value is stored in the database column. The default value is `EnumType.ORDINAL`, which stores the ordinal value of the enum type in the database column. Alternatively, you can set the value to `EnumType.STRING`, which stores the string value of the enum type in the database column. Several things to consider on choosing `EnumType.ORDINAL` or `EnumType.STRING`:

`EnumType.STRING`:

- Readability: When using `EnumType.STRING`, the stored value in the database is the string representation of the enum constant, which makes it more readable than the integer values of EnumType.ORDINAL. This can be particularly useful when the values are used in reports or other user-facing parts of the application.
- Flexibility: Using `EnumType.STRING` makes it possible to change the order or names of the enum constants without affecting the database. This is because the database values are determined by the string representations of the constants, which can be changed without affecting the database schema.
- Stability: Enum constants in Java are assigned a unique ordinal value based on their position in the enum declaration. This means that if you add, remove or reorder the constants, their ordinal values will change. This can cause issues if you're using `EnumType.ORDINAL` because the values stored in the database will no longer match the new ordinal values of the constants.

`EnumType.ORDINAL`:

- Efficiency: Storing enums as integers using `EnumType.ORDINAL` can be more efficient in terms of storage space and retrieval time, as integers take up less space than strings.
- Simplicity: Using `EnumType.ORDINAL` can be simpler to use and implement, as there's no need to worry about string representation or mapping. Additionally, some developers prefer using integers for enum values because they can be used in mathematical operations.
- Consistency: When using `EnumType.ORDINAL`, the database values are always consistent with the enum values in the Java code, as the integers correspond directly to the order in which the enum constants are declared. This can be helpful in ensuring that the database values are always up-to-date with any changes made to the enum values.

### Equivalence Between the SQL Type and Java Type

| SQL Type              | Java Type                                                                                                                             |
|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| BIGINT                | long<br>java.lang.Long                                                                                                                |
| BIT                   | boolean<br>java.lang.Boolean                                                                                                          |
| CHAR                  | char<br>java.lang.Character                                                                                                           |
| CHAR (e.g. 'T', 'f')  | boolean<br>java.lang.Boolean                                                                                                          |
| DOUBLE                | double<br>java.lang.Double                                                                                                            |
| FLOAT                 | float<br>java.lang.Float                                                                                                              |
| INTEGER               | int<br>java.lang.Integer                                                                                                              |
| INTEGER (e.g. 0 or 1) | boolean<br>java.lang.Boolean                                                                                                          |
| SMALLINT              | short<br>java.lang.Short                                                                                                              |
| TINYINT               | byte<br>java.lang.Byte                                                                                                                |
| CLOB                  | java.lang.String                                                                                                                      |
| NCLOB                 | java.lang.String                                                                                                                      |
| CHAR                  | java.lang.String                                                                                                                      |
| VARCHAR               | java.lang.String                                                                                                                      |
| NVARCHAR              | java.lang.String                                                                                                                      |
| LONGVARCHAR           | java.lang.String                                                                                                                      |
| LONGNVARCHAR          | java.lang.String                                                                                                                      |
| DATE                  | java.util.Date<br>java.util.Calendar<br>java.time.LocalDate                                                                           |
| TIME                  | java.util.Date<br>java.sql.Time<br>java.time.OffsetTime<br>java.time.LocalTime                                                        |
| TIMESTAMP             | java.util.Date<br>java.sql.Timestamp<br>java.util.Calendar<br>java.time.Instant<br>java.time.LocalDateTime<br>java.time.ZonedDateTime |
| BIGINT                | java.time.Duration                                                                                                                    |
| VARBINARY             | byte[ ], java.lang.Byte[ ], java.io.Serializable                                                                                      |
| BLOB                  | java.sql.Blob                                                                                                                         |
| CLOB                  | java.sql.Clob                                                                                                                         |
| NCLOB                 | java.sql.Clob                                                                                                                         |
| LONGVARBINARY         | byte[ ], java.lang.Byte[ ]                                                                                                            | 

## Constructor

JPA requires a no-argument constructor method that is either public or protected. A constructor without id field is needed for creating new entries. When the id field is null, it tells JPA we want to create a new row in the table. You can use `@NoArgsConstructor` from Lombok to create no-argument constructor. 

`entities/User.java`

```java
@Entity
@Table(name = "users")
public class User {
  // omitted code

  protected User() {}

  public User(
    String firstName, String lastName, String email, String username,
    String password, Boolean isEnabled
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = password;
    this.isEnabled = isEnabled;
  }

  // omitted code
}
```

## Getter & Setter

Using getter & setter are strongly recommended. It is widely accepted as best practice in Java programming. Spring Data JPA uses it to access and modify the properties of an entity. Another reasons to use getter and setter:

- Encapsulation: Encapsulate the implementation details of your class by hiding the internal state of your object. This means that other classes can access the state of your object only through the getter and setter methods, which helps to protect the integrity of your object.
- Control access: You can define the access level of your getter and setter methods (public, protected, or private), which allows you to restrict or grant access to your object's state as needed.
- Flexibility: Getter and setter methods provide a flexible way to modify your object's state. You can add logic to your getter and setter methods to perform validations, calculations, or other operations on your object's state.
- Compatibility: Supported by many third-party libraries and frameworks. Using getter and setter methods makes your code more compatible with other libraries and frameworks.
- Maintainability: Make your code more readable and maintainable by providing a clear and consistent way to access and modify object's state.

`entities/User.java`

```java
@Entity
@Table(name = "users")
public class User {
  // omitted code

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  // omitted code
}
```

The naming scheme of setter and getter should follow the Java bean naming convention as  `getXxx()` and `setXxx()`, where `Xxx` is the name of the variable. If the variable is of the type boolean, then the getter’s name can be either `isXxx()` or `getXxx()`, but the former naming is preferred.

> Lombok also provide useful annotations that can help creating entity:
> - [Data annotation](https://projectlombok.org/features/Data)
> - [Builder annotation](https://projectlombok.org/features/Builder)
> - [Constructor annotation](https://projectlombok.org/features/constructor)

## `toString()`, `equals()`, `hashCode()`

Another thing to consider with entities is the override of the `hashCode()` and `equals()`
methods to prevent any conflicts with the object’s content. These methods help to know if two instances of an entity are identical so refer to the same row of a table. If you don’t declare all the comparisons between two or more instances of an entity, compare the position in memory, which could be different.

Override `toString()` to provide a more meaningful string representation of the object.

## Types of Inherence

### Mapped Superclass

`@MappedSuperclass` annotation indicates that the class is not a final entity, so it does not exist in the database. Instead, this class is part of another class that inherits it.

```java
@MappedSuperclass
public abstract class Base implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    public Base(){}
    
  public Base(Long id) {
        this.id = id;
    }
  
    public Long getId() {
        return id;
    }
  
    public void setId(Long id) {
        this.id = id;
    }
}
```

### Table per Class Hierarchy

TBA

[//]: # (TODO: penjelasan Table per Class Hierarchy)

### Table per Subclass with Joins

TBA

[//]: # (TODO: penjelasan Table per Subclass with Joins)

### Table per Class

TBA

[//]: # (TODO: penjelasan Table per Class)

## Embeddable Class

An embeddable class is a non-entity class that can be embedded within an entity class as a component. This means that an embeddable class can contain a set of fields that can be reused across multiple entities. Instead of defining these fields separately in each entity, you can create an embeddable class and reuse it across multiple entities.

To define an embeddable class in JPA, you need to annotate it with the `@Embeddable` annotation. This tells JPA that the class should be treated as an embeddable component that can be used within other entities. You can then use this class within an entity by annotating the relevant field or property with the `@Embedded` annotation.

Using an embeddable class can help reduce code duplication and improve the maintainability of your code. It also allows you to group related fields together and reuse them across multiple entities.

`entities/Audit.java`

```java
@Embeddable
public class Audit implements Serializable {
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "updated_on", nullable = true) //Need to be null because the first time this attribute not have a value, only with the modifications have a value.
    private LocalDateTime updatedOn;
    // Attributes, constructors, setters, and getters for all the attributes
    // Override the hashcode and equals
}
```

`entities/User.java`

```java
@Entity
@Table(name = "users")
public class User {
    @Embedded
    private Audit audit;
    // Attributes, constructors, setters, and getters for all the attributes
   // Override the hashcode and equals
}
```
