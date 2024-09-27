package com.codecool.codecooljobhomework.source.repository;

import com.codecool.codecooljobhomework.source.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {

    @Query("SELECT s.id from Source s")
    List<Long> findAllIds();

    List<Source> findAllByIdIn(List<Long> missingSourceIds);
}
