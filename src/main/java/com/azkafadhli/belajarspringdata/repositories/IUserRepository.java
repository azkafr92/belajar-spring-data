package com.azkafadhli.belajarspringdata.repositories;

import com.azkafadhli.belajarspringdata.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserRepository extends JpaRepository<User, String> {

    @Query("SELECT email, username FROM User u WHERE u.email = :emailOrUsername OR u.username = :emailOrUsername")
    List<User> findByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);

}
