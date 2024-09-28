package com.codecool.codecooljobhomework.target.repository;

import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import com.codecool.codecooljobhomework.target.entity.codecooler.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeCoolerRepository extends JpaRepository<Codecooler, Long> {
    Codecooler findByEmail(String email);

    Optional<Codecooler> findByIdAndPosition(long studentId, Position position);
}
