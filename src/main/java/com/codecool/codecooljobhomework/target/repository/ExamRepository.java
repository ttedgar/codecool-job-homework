package com.codecool.codecooljobhomework.target.repository;

import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

}
