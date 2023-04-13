# Relation Between Entities

## Contents:

- [Why need to create multiple entity with relation?](#why-need-to-create-multiple-entity-with-relation)
  - [Database Normalization](#database-normalization)
  - [Entity Relation in Spring Data JPA](#entity-relation-in-spring-data-jpa)
- [Properties of Relation Annotation](#properties-of-relation-annotation)
  - [fetch](#fetch)
  - [mappedBy](#mappedby)
  - [cascade](#cascade)
- [Implementing *One-to-Many* or *Many-to-One* Relation](#implementing-one-to-many-or-many-to-one-relation)
- [Implementing *Many-to-Many* Relation](#implementing-many-to-many-relation)
- [Implementing *One-to-One* Relation](#implementing-one-to-one-relation)

## Why need to create multiple entity with relation?

When you define the structure of your database, many tables have a relationship with others to reduce the number of redundant information. The process of organizing data in a database to reduce redundancy and improve data integrity is called Database Normalization. Before we start to implementing relation between entity, lets talk about with database normalization first to know why need to create multiple entity with relation.

### Database Normalization

Database normalization involves breaking down a database into multiple related tables and establishing relationships between them. The aim of normalization is:
- eliminate data duplication
- minimize data modification anomalies,
- ensure data consistency

Normalization is usually achieved through a series of steps known as normal forms. The most commonly used normal forms are:

**First Normal Form/1NF (Repeating group)** : Repeating groups occur when a table contains multiple instances of a related set of data within a single record, such as multiple phone numbers or email addresses. To eliminate repeating groups and bring a table into 1NF, the related data should be extracted into a separate table and linked to the original table through a foreign key.

**Second Normal Form/2NF (Functionally dependent)** : Functional dependency is a relationship between two attributes in a table, where the value of one attribute determines the value of another. In other words, if we know the value of one attribute, we can determine the value of another. For example, in a table of orders, the price of a product is functionally dependent on its product ID, because the price of a specific product is uniquely determined by its ID. Notice that the functional dependency does not necessarily hold in the reverse direction. For example, any given first or last name may be associated with more than one customer number. (It would be unusual to have a customer table of any size without some duplication of names.)

**Third Normal Form/3NF (Transitive dependency)** : Transitive dependency is a situation in which a non-key attribute in a table is dependent on another non-key attribute, rather than on the primary key. This means that changes to the non-key attribute will affect the values of other non-key attributes. For example, in a table of orders, if the price of a product is dependent on its category, which is in turn dependent on its brand, then there is a transitive dependency between the price and the brand attributes. To remove this dependency and achieve third normal form, the category attribute would need to be separated into a new table with its own primary key.

**Fourth Normal Form/4NF (Multivalued Dependency)** : This requires that a table must have no multivalued dependencies. A multivalued dependency exists when for each value of attribute A, there exists a finite set of values of attribute B that are associated with it, and a finite set of values of attribute C that are also associated with it. Attributes B and C are independent of each other.

There are additional normal forms beyond 4NF, but they are less commonly used in practice.

#### Example case of database normalization

Consider a database for a library that stores information about `books`, `authors`, and `publishers`. The initial design might include a single table called `books` that contains all the information about each book, such as `title`, `author`, `publisher`, `publication_year`, and `ISBN`.

**Initial Design**

| Table | Column                                                            |
|-------|-------------------------------------------------------------------|
| books | id, title, description, author, publisher, publication_year, ISBN | 

However, this design violates the first normal form (1NF) because some columns contain multiple values. For example, the `author` column might contain multiple authors separated by commas. This can lead to data duplication and inconsistencies, such as misspelled author names or multiple records for the same book with slightly different author lists.

To address this issue, we can break the `books` table into two separate tables: `authors` and `books`. The `authors` table would contain information about each author, such as `name`, `birthdate`, and `nationality`. The `books` table would contain information about each book, such as `title`, `publisher`, `publication_year`, and `ISBN`, as well as a foreign key that references the `authors` table (`author_id`).

##### First Normal Form (1NF)

| Table   | Column                                                               |
|---------|----------------------------------------------------------------------|
| authors | id, name, birthdate, nationality                                     |
| books   | id, title, description, publisher, publication_year, ISBN, author_id |

However, this design still violates the second normal form (2NF) because non-key attributes (e.g., publication year) are not fully dependent on the primary key. For example, if there are multiple editions of a book with different publication years, the publication year would need to be duplicated in each record. This can lead to data modification anomalies, such as inconsistencies between different records for the same book.

To address this issue, we can further break the `books` table into three separate tables: `authors`, `books`, and `editions`. The `authors` table would remain the same, while the `books` table would contain information about each book, such as title and publisher, as well as a foreign key that references the `authors` table. The `editions` table would contain information about each edition of each book, such as publication year and ISBN, as well as foreign keys that reference both the `books` and `authors` tables.

##### Second Normal Form (2NF)

| Table    | Column                                       |
|----------|----------------------------------------------|
| authors  | id, name, birthdate, nationality             |
| books    | id, title, description, publisher, author_id |
| editions | id, book_id, publication_year, ISBN          |

This design satisfies both 1NF and 2NF, but it still violates the third normal form (3NF) because there is a transitive dependency between the `books` and `editions` tables. Specifically, the `publisher` attribute in the `books` table is functionally dependent on the `publisher` attribute in the `editions` table, which is in turn functionally dependent on the primary key (i.e., the combination of "ISBN" and "publication year") in the `editions` table.

To address this issue, we can break the `books` table into a fourth table called `publishers`, which contains information about each publisher, such as name and address. The `editions` table would then contain a foreign key that references the `publishers` table, rather than the `books` table. This design satisfies 1NF, 2NF, and 3NF, and ensures that each piece of data is stored only once and in a consistent manner.

##### Third Normal Form (3NF)

| Table      | Column                                            |
|------------|---------------------------------------------------|
| authors    | id, name, birthdate, nationality                  |
| books      | id, title, description, author_id                 |
| editions   | id, book_id, publisher_id, publication_year, ISBN |
| publishers | id, name, address                                 |

Now, we assume that each book can have multiple authors and each author can have written multiple books. So there is a Multivalued Dependency problem between Books and Authors tables. To solve this problem, we need to split the Books table into two tables, `books` and `book_author`.

#### Fourth Normal Form (4NF)
| Table        | Column                                            |
|--------------|---------------------------------------------------|
| authors      | id, name, birthdate, nationality                  |
| books        | id, title, description                            |
| book_authors | book_id, author_id                                |
| editions     | id, book_id, publisher_id, publication_year, ISBN |
| publishers   | id, name, address                                 |

By creating the `book_authors` table, we have removed the multivalued dependency between `books` and `authors` tables. We can store multiple authors for each book in the `book_authors` table without repeating the book title for each author.

### Entity Relation in Spring Data JPA

Entity relationships in Spring Data JPA allow you to define how different entities in your application are related to each other. There are three main types of entity relationships in JPA:

- One-to-One: This type of relationship is used when an entity is associated with exactly one instance of another entity. In JPA, you can create a one-to-one relationship between two entities by using the `@OneToOne` annotation.
- One-to-Many: This type of relationship is used when an entity is associated with multiple instances of another entity. In JPA, you can create a one-to-many relationship between two entities by using the `@OneToMany` annotation and the other entity has `@ManyToOne` annotation.
- Many-to-Many: This type of relationship is used when an entity can be associated with multiple instances of another entity and vice versa. In JPA, you can create a many-to-many relationship between two entities by using the `@ManyToMany` annotation.

## Properties of Relation Annotation

### fetch

At default, when you add the relationship between two or more entities, the endpoint response gives all the information that could not be necessary in all cases. JPA offers a mechanism to reduce the number of data in memory until you need it. The way to do it is to add a property `fetch` in the annotation that indicates the relationship between both entities. The property has two potential values.

- `FetchType.LAZY`: indicates the implementation of JPA that is not necessary to obtain the information of the relationship until someone invokes the attribute’s get method. This approach spends less memory in the application and gives you a faster load of information; in the other hand, if you need to obtain always information about the relationship, the cost of executing the operation increases and takes more time.

- `FetchType.EAGER`: indicates the JPA implementation that must obtain all the other entity’s information when executing the query. With this approach, you reduce the time to initialization because when you have one entity in memory, you have all the information; in the other hand, the query execution could take more time and negatively impact the application’s performance.

### mappedBy

The `mappedBy` property is used to specify the mapping of the bidirectional relationship between two entities. When you have a bidirectional relationship between two entities, you need to specify how the two sides of the relationship are mapped to each other. For example, if you have an `User` entity that has one-to-many relationship with `Address` entities, you might also want to have a reference from each `Address` back to its `User`.

```java
@Entity
@Table(name = "users")
public class User {
  // omitted code

  @OneToMany(mappedBy = "user")
  private List<Address> addresses;

  // omitted code
}

@Entity
@Table(name = "addresses")
public class Address {
  // omitted code

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;

  // omitted code
}
```

In a One-to-Many/Many-to-One relationship, the owning side is usually defined on the many side of the relationship. It's usually the side that owns the foreign key. Entity that has `@ManyToOne` is the owning side and has `@JoinColumn` annotation. The other entity has `OneToMany` annotation with `mappedBy` properties (non-owning side, inverse or the referencing side).

Using `mappedBy` can simplify the mapping of bidirectional relationships, as it allows you to avoid defining redundant mapping information on both sides of the relationship. It also ensures that the relationship is synchronized correctly when you update either side of the relationship.

> The use of the properties updatable and insertable in the `@JoinColumn` annotation with false indicates that it is not the entity’s responsibility to modify the connected entities.

### cascade

When you define an entity relationship in Spring Data JPA, you must also define the cascading behavior for the relationship. The cascading behavior determines what actions should be taken on associated entities when an entity is created, updated, or deleted. There are six types of cascading behaviors:

- `CascadeType.ALL`: associated entities should be created, updated, or deleted when the parent entity is created, updated, or deleted.
- `CascadeType.PERSIST`: associated entities should be created when the parent entity is created.
- `CascadeType.MERGE`: associated entities should be updated when the parent entity is updated.
- `CascadeType.REMOVE`: associated entities should be deleted when the parent entity is deleted.
- `CascadeType.REFRESH`: associated entities should be refreshed when the parent entity is refreshed, it means that the values of its attributes are reloaded from the database, as it requires reloading the values of associated entities from the database, this type can be an expensive operation.
- `CascadeType.DETACH`: associated entities should be detached when the parent entity is detached, means that it is removed from the persistence context and is no longer managed by the JPA provider.

> read more: https://www.baeldung.com/jpa-cascade-types

## Implementing *One-to-Many* or *Many-to-One* Relation

Here, one user can have many addresses. One-to-Many relation annotated with `@OneToMany`, and `@ManyToOne` for Many-to-One relation.

`entities/User`

```java
@Entity
@Table(name = "users")
public class User {
  // omitted code

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
  private List<Address> addresses;

  // omitted code
}
```

`entities/Address`

```java
@Entity
@Table(name = "addresses")
public class Address {
  // omitted code

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;

  // omitted code
}

```

In the `User` entity, there is a field named `addresses` that is annotated with `@OneToMany`. This annotation creates a one-to-many relationship between `User` and `Address` entities. The `fetch` attribute of the annotation specifies that `addresses` should be lazily loaded, which means they will be loaded only when accessed for the first time.

The `mappedBy`attribute of the `@OneToMany` annotation specifies the field in the `Address` entity that maps the relationship. In this case, the `user` field in the `Address` entity maps the relationship to the `User` entity.

The `cascade` attribute of the `@OneToMany` annotation specifies that the cascade behavior should be applied when performing operations on the User entity. In this case, the `CascadeType.ALL` value specifies that any operation (create, update, delete) performed on the `User` entity should also be cascaded to the associated Address entities.

In the `Address` entity, there is a field named `user` that is annotated with `@ManyToOne`. This annotation creates a many-to-one relationship between `Address` and `User` entities. The `JoinColumn` annotation specifies the name of the foreign key column in the `Address` entity that references the primary key column of the `User` entity. The `nullable` attribute specifies whether the foreign key column can contain null values.

The `@JsonIgnore` annotation is used to instruct Jackson, a JSON serialization and deserialization library used by Spring, to ignore this field during serialization and deserialization. This is usually done to avoid circular references or to hide sensitive information. In this case, it prevents the `User` entity from being serialized as part of the `Address` entity, which could result in an infinite loop.

> further read: https://www.baeldung.com/hibernate-one-to-many

## Implementing *Many-to-Many* Relation

`entities/User`

```java
@Entity
@Table(name = "users")
public class User {
  // omitted code

@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(
        name = "user_tags",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")}
)
private Set<Tag> tags;

  // omitted code
}
```

`entities/Tag`

```java
@Entity
@Table(name = "tags")
public class Tag {
  // omitted code

@ManyToMany(mappedBy = "tags")
@JsonIgnore
private List<User> users;

  // omitted code
}
```

The `User` entity has a field named `tags` that is annotated with `@ManyToMany`. This annotation creates a many-to-many relationship between `User` and `Tag` entities. The `fetch` attribute specifies that the `Tag` entities should be lazily loaded, which means they will be loaded only when accessed for the first time. The `JoinTable` annotation specifies the name of the join table that will be used to store the relationship between `User` and `Tag` entities. It also specifies the foreign key columns that will reference the primary keys of the `User` and `Tag` entities.

The `Tag` entity has a field named `users` that is annotated with `@ManyToMany`. This annotation creates a many-to-many relationship between `User` and `Tag` entities. The `mappedBy` attribute specifies the field in the `User` entity that maps the relationship. In this case, the `tags` field in the `User` entity maps the relationship to the Tag entity. The `JsonIgnore` annotation specifies that this field should be ignored during serialization to avoid infinite recursion.

> further read: https://www.baeldung.com/hibernate-many-to-many

## Implementing *One-to-One* Relation

`entities/User`

```java
@Entity
@Table(name = "users")
public class User {
  // omitted code

@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
private UserIdentity userIdentity;

  // omitted code
}
```

`entities/UserIdentities`

```java
@Entity
@Table(name = "user_identities")
public class UserIdentity {
  // omitted code

@Id
@Column(name = "user_id")
private UUID id;

@OneToOne
@MapsId
@JoinColumn(name = "user_id")
private User user;

  // omitted code
}
```

In the `User` entity, there is a field named `userIdentity` that is annotated with `@OneToOne`. This annotation creates a one-to-one relationship between User and UserIdentity entities. The "mappedBy" attribute of the annotation specifies the field in the UserIdentity entity that maps the relationship. In this case, the "user" field in the UserIdentity entity maps the relationship to the User entity. The "fetch" attribute specifies that the UserIdentity should be lazily loaded, which means it will be loaded only when accessed for the first time.

In the UserIdentity entity, there is a field named "user" that is annotated with @OneToOne. This annotation creates a one-to-one relationship between User and UserIdentity entities. The "MapsId" annotation specifies that the primary key of the User entity should be used as the primary key of the UserIdentity entity. The "JoinColumn" annotation specifies the name of the foreign key column in the UserIdentity table that references the primary key column of the User entity.

The "@Id" annotation on the "id" field of the UserIdentity entity specifies that this field is the primary key of the UserIdentity entity. The "@Column" annotation specifies the name of the column in the UserIdentity table that corresponds to this field.

The overall effect of this mapping is that there is a one-to-one relationship between User and UserIdentity entities, where the primary key of the User entity is used as the primary key of the UserIdentity entity. The "mappedBy" attribute in the User entity specifies that the relationship is mapped by the "user" field in the UserIdentity entity. This means that any updates to the User entity will be reflected in the UserIdentity entity, and vice versa. The "fetch" attribute in the User entity specifies that the UserIdentity entity should be lazily loaded.

> further read: https://www.baeldung.com/jpa-one-to-one#spk-strategy
