# Belajar Spring Data

## Daftar Isi
 
- [Project Setup](#project-setup)
- [Repository Pattern](#repository-pattern)
- [Pengenalan Spring Data](#pengenalan-spring-data)
- [Setup `application.yml`](#setup-applicationyml)
- [ERD](#erd)
- [Entities](#entities)
  - [Implementasi Entity](#implementasi-entity) 
  - [Implementasi relasi *One to Many*](#implementasi-relasi-one-to-many)
  - [Implementasi relasi *Many to Many*](#implementasi-relasi-many-to-many)
  - [Implementasi relasi *One to One*](#implementasi-relasi-one-to-one)
- [Repositories](#repositories)
- [DTOs](#dtos)

## Project Setup

Di bawah ini adalah setup yang digunakan:

- Java 17
- Spring Boot 3.0.1
- Maven
- Packaging: JAR

**Dependencies:**

- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Lombok
- Spring Configuration Processor
- Flyway Migration

Atau klik [tautan](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.0.1&packaging=jar&jvmVersion=17&groupId=com.azkafadhli&artifactId=belajarspringdata&name=belajarspringdata&description=Alterra%20Academy%20-%20Belajar%20Spring%20Data&packageName=com.azkafadhli.belajarspringdata&dependencies=web,data-jpa,postgresql,lombok,configuration-processor,flyway) ini untuk membuat setup yang sama.

## Pengenalan Spring Data

[//]: # (TODO: penjelasan mengenai Spring Data)pen

## Repository Pattern

[//]: # (TODO: penjelasan mengenai repository pattern)

## Setup `application.yml`

Ubah file `application.properties` menjadi `application.yml`.

> Lihat [di sini](https://www.baeldung.com/spring-boot-yaml-vs-properties) untuk mengetahui perbandingan kedua tipe tersebut.

Berikut setup yang diperlukan untuk melakukan koneksi ke database.

```yaml
spring:
  datasource:
    url: "jdbc:postgresql://localhost:5432/belajar_spring_data"
    username: postgres
    password: 1234567890
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    generate-ddl: true
    properties:
      database: postgresql
      database-platform: org.hibernate.dialect.PostgreSQLDialect
```

Kita menggunakan `ddl-auto: create`.
Setiap kali aplikasi dijalankan akan menghapus skema lama jika sudah ada dan dibuat skema baru.
Pilihan lainnya:

- `validate`: tidak membuat skema baru, hanya memvalidasi apakah skema sudah sesuai dengan konfigursi
- `update`: update skema
- `create`:  ketika dijalankan akan menghapus skema lama, membuat skema baru
- `create-drop`: menghapus skema setiap kali aplikasi dihentikan, dan akan membuat skema baru jika aplikasi dijalankan

Pastikan database PostgreSQL sudah dijalankan, Lalu coba jalankan aplikasi untuk memastikan tidak ada error.
Pada bagian selanjutnya, kita akan membuat entity.

## ERD

[//]: # (TODO: put entity relation diagram here, `user` and `tag` has many-to-many relation_)

## Entities

> `Entity`: A class whose purpose is to store/retrieve data to/from a data store.

Buat sebuah package bernama `entities`. Di dalamnya buat tiga kelas baru bernama `User`, `Address`, dan `Tag`.

`User`
```java
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;

    private String lastName;

    // email & name will be mapped to column with same name
    // if no name set in annotation @Column
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "boolean default false")
    private Boolean isEnabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_tags",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private List<Address> address;

}
```

[//]: # (TODO: enable soft delete in `User` entity)

`Address`
```java
@Entity
@Table(name = "addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
}
```

[//]: # (TODO: enable soft delete in `Address` entity)

`Tag`
```java
@Table(name = "tags")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<User> users;

    protected Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

}
```

[//]: # (TODO: enable soft delete in `Tag` entity)

### Implementasi *entity*

JPA requires a no-argument constructor method that is either public or protected.
We also have a constructor where the id field isn’t provided: a constructor designed for creating new entries in the database.
When the id field is null, it tells JPA we want to create a new row in the table.

It has `@Getter` and `@Setter` annotations. For more details explanation of these annotations:
- [Getter]()
- [Setter]()

Another useful annotations:
- [Data annotation](https://projectlombok.org/features/Data)
- [Builder annotation](https://projectlombok.org/features/Builder)
- [Constructor annotation](https://projectlombok.org/features/constructor)

`@Entity` is to make the class an entity that will be recognized by Spring JPA. [Documentation](https://jakarta.ee/specifications/persistence/3.1/apidocs/jakarta.persistence/jakarta/persistence/entity).

`@Table` to override default table name. [Documentation](https://jakarta.ee/specifications/persistence/3.1/apidocs/jakarta.persistence/jakarta/persistence/table).

`@Id` to set the attribute as primary key. JPA recognize this property as object's ID. [Documentation](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1.html#a14827).

`@GeneratedValue` to make id auto generated by whatever strategy we use. ID will be generated automatically. IDENTITY means that ID generation will be maintained by database, SEQUENCE by application. [Documentation](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1.html#a14790).

Using `Spring Boot 3`, make sure `@Entity`, `@Table`, `@Id`, `@GeneratedValue` is imported from `jakarta.persistence`. See the documentation [here](https://jakarta.ee/specifications/persistence/3.1/apidocs/jakarta.persistence/jakarta/persistence/package-summary.html).

| Annotation          | Imported from                      |
|---------------------|------------------------------------|
| @Data               | lombok.Data                        |
| @Builder            | lombok.Builder                     |
| @NoArgsConstructor  | lombok.NoArgsConstructor           |
| @AllArgsConstructor | lombok.AllArgsConstructor          |
| @Entity             | jakarta.persistence.Entity         |
| @Table              | jakarta.persistence.Table          |
| @Id                 | jakarta.persistence.Id             |
| @GeneratedValue     | jakarta.persistence.GeneratedValue |

After you creating these entities, restart application and see the changes in your database.
If no error, table `users` and `tags` automatically created in database.

### Implementasi relasi *One to Many*

> https://www.baeldung.com/hibernate-one-to-many

[//]: # (TODO: penjelasan relasi One to Many)

### Implementasi relasi *Many to Many*

> https://www.baeldung.com/hibernate-many-to-many

[//]: # (TODO: penjelasan relasi Many to Many)

### Implementasi relasi *One to One*

> https://www.baeldung.com/jpa-one-to-one

[//]: # (TODO: penjelasan relasi One to One)

## Repositories

Berikutnya, kita membuat sebuah interface repository untuk mengakses tabel dan melakukan query.
Buatlah sebuah package bernama `repositories`, lalu di dalamnya buat interface `IUserRepository`, `IAddressRepository`, dan `ITagRepository`.

`IUserRepository`
```java
public interface IUserRepository extends JpaRepository<User, String> {
}
```

`IAddressRepository`
```java
public interface IAddressRepository extends JpaRepository<Address, Long> {
}
```

`ITagRepository`
```java
public interface ITagRepository extends JpaRepository<Tag, Long> {
}
```

## Services

Langkah selanjutnya adalah membuat service yang menggunakan repository.
Buat sebuah package bernama `Services`, lalu di dalamnya buat sebuah interface `IUserService` dan kelas yang mengimplementasikannya `UserService`.

`IUserService`
```java
public interface IUserService {
}
```

`UserService`
```java
public class UserService implements IUserService {
}
```

## DTOs

`DTO`: Data Transfer Object. A class whose purpose is to transfer data, usually from server to client (or vice versa).

### Why DTO?

For more details explanation, see [here](https://www.baeldung.com/java-dto-pattern).

### `BaseResponse`
```java
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse <T> {
    private final int code;

    private final String message;

    private final T data;

    private Pagination pagination;

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int code, String message, T data, Pagination pagination) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }
}
```

### `SuccessResponse`

### `CreateUserRequest`
```java
@Getter
@Setter
public class CreateUserRequest implements Serializable {

    @JsonProperty(required = true)
    private String firstName;

    @JsonProperty(required = true)
    private String lastName;

    @JsonProperty(required = true)
    private String email;

    @JsonProperty(required = true)
    private String password;

    @JsonProperty(defaultValue = "false")
    private Boolean isEnabled;

    @JsonProperty(required = true)
    private Set<String> authority;

}
```