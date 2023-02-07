# Transaction

## Contents:

- [What is Transaction?](#what-is-transaction)
- [Transaction in Spring Data](#transaction-in-spring-data)

## What is Transaction?

Transaction is a set of write and read operations grouped for criteria that need to be executed to ensure that all
operations are executed or not one of them.

> A transaction in a database starts with the `BEGIN_TRANSACTION` keyword and needs to indicate that all the operations
> end successfully using the `COMMIT` keyword. So, the database takes this instruction and ensures this is the new state
> for the affected rows. On the other hand, when operations fail, you need to invoke the `ROLLBACK` keyword to indicate
> that the database needs to back to the previous state before all the operations are executed. Example in Postgresql:
>
> ```
> BEGIN;
> 
> UPDATE accounts
>   SET balance = balance - 1000
> WHERE account_no = 100;
> 
> UPDATE accounts
>   SET balance = balance + 1000
> WHERE account_no = 200;
> 
> COMMIT;
> ```

## Transaction in Spring Data

Spring 3.1 introduces the `@EnableTransactionManagement` annotation that we can use in a `@Configuration` class to
enable transactional support. However, if we're using a Spring Boot project and have a `spring-data-*` or `spring-tx`
dependencies on the classpath, then transaction management will be enabled by default.

`@Transactional` is an important annotation that you can use in a method, class, or interface which creates a proxy
class
that has the responsibility to create the transaction and commit/roll back all the operations inside the transaction.
This annotation is connected directly with Spring TX (org.springframework.transaction.annotation.Transactional), which
is not the same as JEE. This annotation only works for the public methods because it’s a constraint of the proxy classes
of Spring Framework , so if you put the annotation in the private/protected Spring Framework, you ignore them without
showing any error on the console or in your IDE.

### `@Transactional` Properties

We can annotate a bean with `@Transactional` either at the class or method level. The annotation support further
configuration using properties as well.
See the
documentation [here](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Transactional.html)
.
More details explanation [here](https://www.baeldung.com/transaction-configuration-with-jpa-and-spring).

- `isolation`: ensures that each transaction operates independently and in isolation from other transactions, even if
  they are executing concurrently. This means that a transaction's changes to the database are not visible to other
  transactions until the transaction is committed. The level of isolation can be controlled through the use of isolation
  levels, which determine how data modifications made by one transaction affect other transactions. Available isolation
  level:
    - `Isolation.DEFAULT`: Read Committed is the default isolation level in Postgresql.
    - `Isolation.SERIALIZABLE`: https://www.vertica.com/docs/9.3.x/HTML/Content/Resources/Images/Transactions/SERILIZABLETimeline.png
      .
      The Serializable isolation level is the highest level of transaction isolation in SQL. It guarantees that
      transactions are executed in a way that ensures they appear to be executed serially, as if they were executed one
      after the
      other, even if they are executed concurrently by multiple transactions. In this level of isolation, transactions
      are prevented from accessing data that has been modified but not yet committed by other transactions. This ensures
      that data consistency is maintained, even in multi-user environments. If a transaction attempts to modify data
      that has been modified by another transaction, it will be blocked until the first transaction is committed or
      rolled back.
      This ensures that the data remains in a consistent state and that the changes made by one transaction are not
      overwritten by another. In summary, Serializable isolation level provides the strongest level of consistency and
      isolation for transactions, but it also comes with the highest cost in terms of performance, as it requires the
      most locking and blocking, leading to reduced concurrency.
    - `Isolation.READ_UNCOMMITTED`: The "Read Uncommitted" isolation level is the lowest level of isolation in a
      database management system. This isolation level allows transactions to read data that has been modified by other
      transactions that have not yet been committed. This means that a transaction can read data that is still being
      changed by another transaction, leading to what is known as "dirty reads." As a result, the data read by a
      transaction at the Read Uncommitted isolation level may not be accurate or consistent, since it could be changed
      by another transaction that has not yet been committed. Because of the potential for incorrect or inconsistent
      data, the Read Uncommitted isolation level is generally not recommended for use in production systems where data
      consistency and accuracy are critical. However, it may be useful in certain situations where the highest level of
      data consistency is not required.
    - `Isolation.READ_COMMITTED`: In the Read Committed isolation level, a transaction can only read data that has been
      committed by other transactions. This means that any data changes made by other transactions are not visible to
      the current transaction until the changes have been committed. This ensures that a transaction always sees a
      consistent view of the data, but may result in a phenomenon known as "dirty read," where the current transaction
      reads uncommitted data from another transaction that may later be rolled back. The Read Committed isolation level
      is the most commonly used isolation level and provides a good balance between consistency and concurrency.
    - `Isolation.REPEATABLE_READ`: The Repeatable Read isolation level in SQL provides a guarantee that all data read by
      a transaction will remain constant throughout the transaction's duration, even if other transactions
      simultaneously modify the data. In other words, if a transaction reads a row of data, it can be certain that the
      same row will be read again if it is needed later in the transaction, and that the data will not have changed due
      to another transaction's updates. This level of isolation is achieved by locking the rows that are read by a
      transaction and preventing other transactions from modifying them until the original transaction completes. This
      ensures that any subsequent read operation in the transaction returns the same data as the first read, even if
      other transactions modify the data in the meantime. However, it should be noted that the Repeatable Read level can
      lead to increased locking and potential deadlocks, as multiple transactions may lock the same rows and block each
      other from making further updates.

- `readOnly`: `Boolean`. If the transaction is read-only, the framework and the database introduce optimizations to this
  type of
  operation. The main problem with the incorrect use of this property appears when you use it in methods that write
  modifications in the database. It does not persist, and the modifications are lost. More use case of this properties
  can
  be read [here](https://vladmihalcea.com/read-write-read-only-transaction-routing-spring/).

- `rollbackFor`: `Class<? extends Throwable>[]`. If no custom rollback rules are configured in this annotation, the
  transaction will roll back on
  `RuntimeException` and `Error` but not on checked exceptions. You also can skip an exception using `noRollbackFor`.

- `timeOut`: Timeout is expressed in milliseconds (`int`)

- `propagation`: "Propagation" in Spring Data refers to the behavior of transactions when multiple transactions are
  involved in a single operation. Available options:
    - `REQUIRED`: if a transaction is already active, the annotated method will execute within that transaction. If no
      transaction is active, a new transaction will be started.
    - `SUPPORTS`: the annotated method will execute within an existing transaction, if one exists. If no transaction is
      active, the method will be executed without a transaction.
    - `MANDATORY`: the annotated method must execute within an existing transaction. If no transaction is active, a "
      TransactionRequiredException" will be thrown.
    - `REQUIRES_NEW`: when a transaction is in progress, it pauses it, and a new one starts; but if there is no
      transaction in progress, it creates a new one.
    - `NOT_SUPPORTED`: variant of the previous scenario because if a transaction is in progress, it pauses, and all the
      transactional methods are executed without a transaction.
    - `NEVER`: if a transaction is running when a method with this property, IllegalTransactionStateException is thrown.
    - `NESTED`: when "Propagation.NESTED" is used, a nested transaction is created whenever a transaction is already
      active. The nested transaction will have its own separate transaction context, and any changes made within the
      nested transaction will be committed or rolled back independently of the outer transaction. If the nested
      transaction completes successfully, it will be committed and the changes made within it will become a permanent
      part of the outer transaction. If the nested transaction fails and is rolled back, the outer transaction will
      remain intact and the changes made within the nested transaction will be discarded. If there is no existing
      transaction when the method annotated with "Propagation.NESTED" is called, a new transaction will be started, just
      like with "Propagation.REQUIRED". It is important to note that not all database systems support nested
      transactions, so the exact behavior of "Propagation.NESTED" may depend on the underlying database technology.
