package com.codecool.codecooljobhomework.source.repository;

import com.codecool.codecooljobhomework.source.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {

}
