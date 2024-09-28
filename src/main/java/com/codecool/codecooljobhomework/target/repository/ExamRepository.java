package com.codecool.codecooljobhomework.target.repository;

import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByModuleAndStudentEmail(Module module, String studentEmail);

    @Query("SELECT e.sourceId from Exam e")
    List<Long> findAllSourceIds();

    Exam findFirstByModuleAndStudentEmailAndDateAfter(Module module, String studentEmail, LocalDate currentDate);

    @Query(value = """
                        WITH latestExams AS (
                            SELECT er.dimension, er.result
                            FROM exam e
                            JOIN exam_results er ON e.id = er.exam_id
                            WHERE e."date" = (
                                SELECT MAX(e2.date) 
                                FROM exam e2
                                WHERE e."module" = e2."module"
                                AND e2.student_id = :studentId
                            )
                            AND e.cancelled = false
                        )
                        SELECT l.dimension, AVG(l.result) 
                        FROM latestExams l
                        GROUP BY l.dimension
                    """, nativeQuery = true)
    List<Object[]> findAverageResultsOfLatestExamsByStudentId(@Param("studentId") long studentId);
}
