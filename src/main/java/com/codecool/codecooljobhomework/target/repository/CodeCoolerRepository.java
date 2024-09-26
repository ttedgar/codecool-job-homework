package com.codecool.codecooljobhomework.target.repository;

import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeCoolerRepository extends JpaRepository<Codecooler, Long> {
    Codecooler findByEmail(String email);
}
