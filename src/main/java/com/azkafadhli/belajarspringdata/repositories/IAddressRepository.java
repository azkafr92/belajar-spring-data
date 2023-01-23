package com.azkafadhli.belajarspringdata.repositories;

import com.azkafadhli.belajarspringdata.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddressRepository extends JpaRepository<Address, Long> {
}
