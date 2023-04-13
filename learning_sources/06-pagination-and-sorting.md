# Pagination and Sorting

Pagination is essential in handling large amounts of data by present it in smaller chunks, more manageable pages, without having to load all the data at once. Sorting is needed for arranging the data in particular order, ascending or descending, based on specific attribute. You can use the `Pageable` interface provided by Spring Data. To enable pagination, the repository extend `PagingAndSortingRepository` or `JpaRepository` class. By extending, the repository have method `findAll(Pageable)` and `findAll(Sort sort)`.

You also can create automatic/manual query, and pass `Pageable` into the method. 

`repositories/User.java`

```java
public interface IUserRepository extends JpaRepository<User, String> {

    @Query("SELECT email, username FROM User u WHERE u.email = :emailOrUsername OR u.username = :emailOrUsername")
    List<User> findByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername, Pageable pageable);
}
```

`services/UserService.java`

```java
import org.springframework.data.domain.Sort;

@Service
public class UserService implements IUserService {
    // omitted code
    public UserListDTO getUsers() {
        int page = 0;
        int size = 5;
        Sort sort = Sort.by("username").descending();
        // create Pageable object by passing page, size, and sort
        // you can also pass page & size only
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Call `findAll` method by passing Pageable object
        Page<User> userPage = userRepository.findAll(pageable);
        
        // or passing Sort object
        // List<User> users = userRepository.findAll(sort);
        
        // omitted code
    }
    // omitted code
}
```
