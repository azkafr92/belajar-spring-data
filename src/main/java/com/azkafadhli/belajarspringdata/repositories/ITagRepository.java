package com.azkafadhli.belajarspringdata.repositories;

import com.azkafadhli.belajarspringdata.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITagRepository extends JpaRepository<Tag, Long> {
}
