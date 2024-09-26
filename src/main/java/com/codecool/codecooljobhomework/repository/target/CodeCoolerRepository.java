package com.codecool.codecooljobhomework.repository.target;

import com.codecool.codecooljobhomework.entity.target.codecooler.Codecooler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeCoolerRepository extends JpaRepository<Codecooler, Long> {

}
