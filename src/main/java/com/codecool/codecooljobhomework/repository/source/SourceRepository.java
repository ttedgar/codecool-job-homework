package com.codecool.codecooljobhomework.repository.source;

import com.codecool.codecooljobhomework.entity.source.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {
}
