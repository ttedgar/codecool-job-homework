package com.codecool.codecooljobhomework.repository.target;

import com.codecool.codecooljobhomework.entity.target.exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

}
