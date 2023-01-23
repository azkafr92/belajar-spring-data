package com.azkafadhli.belajarspringdata.repositories;

import com.azkafadhli.belajarspringdata.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, String> {
}
