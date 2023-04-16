# Versioning or Migrating Changes

This chapter discusses tools and strategies that reduce the risk of introducing problems with making changes and how you can integrate these tools with Spring Data to execute the changes. You also learn how to implement a feature flag to reduce the risk of deploying something in production which could fail and need to redeploy the application reverting the changes.

> Read:
> - https://medium.com/javarevisited/database-migration-in-spring-boot-using-flyway-ee791db8aea0

## Contents:

- [Why Using Database Migrating Tool](#why-using-database-migrating-tool)
- [Using `Flyway` Library](#using-flyway-library)

## Why Using Database Migrating Tool?

Using a database migration tool can provide several benefits:

- Version Control: Database migration tools allow you to version your database schema and track changes over time, just like you version your application code. This makes it easier to manage and track changes to the database schema, and to revert to a previous version if necessary.
- Repeatable Migrations: Database migration tools ensure that migrations are repeatable and idempotent, which means that running the same migration multiple times will not cause any unintended consequences.
- Improved Collaboration: By using a migration tool, multiple developers can work on the same database schema, making it easier to manage conflicts and track changes.
- Automated Deployments: Database migration tools can be integrated into your deployment pipeline, making it easier to automate database deployments and reduce the risk of manual errors.
- Better Testability: Database migration tools make it easier to test your database changes, since you can define migrations in code and run them in an isolated environment. This helps to ensure that your migrations are working as expected and reduces the risk of introducing bugs into your database.
- Improved Consistency: By using a migration tool, you can ensure that your database schema is in a consistent state, which makes it easier to catch and fix errors before they cause problems in production.

Using a database migration tool can help you manage your database schema changes more effectively, reducing the risk of errors and improving the overall quality of your database.

Now, in `application.yml` set `jpa:hibernate:ddl-auto` to `validate`. Then drop all tables in database, except `flyway_schema_history`, if any. If you try to run the application, it will give error, like `Schema-validation: missing table`. We will create these tables using migration tool.

## Using `Flyway` Library

Flyway is a well-established and widely used database migration tool that provides a simple and effective way to manage database schema changes. If you need to manage database migrations in your project, Flyway is a good choice to consider.

1. Add dependency in `pom.xml`:
    ```xml
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>
    ```
    Spring Boot will then automatically auto-wire Flyway with its DataSource and invoke it on startup. After application start, you will get new folder `src/main/resources/db/migration`. Flyway will look for scripts under the path `db/migration` folder by default. The naming convention for all the migration scripts is `V[VERSION_NUMBER__[NAME].sql`
2. Create `V1__initialize.sql`
    ```sql
    create sequence tags_seq start with 1 increment by 50;
    
    create table addresses (
      id bigserial not null,
      address TEXT,
      user_id uuid not null,
      primary key (id)
    );
    
    create table tags (
      id bigint not null,
      name varchar(255),
      primary key (id)
    );
    
    create table user_identities (
      user_id uuid not null,
      dob date,
      nik varchar(16),
      first_name varchar(255),
      last_name varchar(255),
      primary key (user_id)
    );
    
    create table user_tags (
      user_id uuid not null,
      tag_id bigint not null,
      primary key (user_id, tag_id)
    );
    
    create table users (
      id uuid not null,
      created_on timestamp(6) not null,
      updated_on timestamp(6),
      authorities varchar(255) array,
      email varchar(255) not null,
      is_enabled boolean default false,
      password varchar(255) not null,
      username varchar(255) not null,
      primary key (id)
    );
     
    ```
3. Run application
4. Create `V2__add_relation.sql`
    ```sql
    alter table
      if exists users
    add
      constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
    
    alter table
      if exists users
    add
      constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);
    
    alter table
      if exists addresses
    add
      constraint FK1fa36y2oqhao3wgg2rw1pi459 foreign key (user_id) references users;
    
    alter table
      if exists user_identities
    add
      constraint FKl8i188j5rgpteq6erbt6x1h0m foreign key (user_id) references users;
    
    alter table
      if exists user_tags
    add
      constraint FKioatd2q4dvvsb5k6al6ge8au4 foreign key (tag_id) references tags;
    
    alter table
      if exists user_tags
    add
      constraint FKdylhtw3qjb2nj40xp50b0p495 foreign key (user_id) references users;
    ```
5. Restart application

You can see migration history in `flyway_schema_history` table.