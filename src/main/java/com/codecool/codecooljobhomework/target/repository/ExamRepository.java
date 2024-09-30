package com.codecool.codecooljobhomework.target.repository;

import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.entity.exam.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT e.sourceId from Exam e")
    List<Long> findAllSourceIds();

    @Query(value = """
                        WITH latestExams AS (
                            SELECT er.dimension, er.result
                            FROM exam e
                            JOIN exam_results er 
                            ON e.id = er.exam_id
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

    @Query(value = """
                        select count(*)
                        from exam e
                        where e.student_id = :studentId
                        and e."module" = :moduleStr
                    """, nativeQuery = true)
    int findNumberOfAttempts(long studentId, String moduleStr);

    @Query(value = """
                        WITH passCount AS (
                            SELECT COUNT(*) AS passes
                            FROM exam e
                            WHERE e.attemptcount = :attemptCount
                            AND e.mentor_id = :mentorId
                            AND e.success
                        ),
                        failCount AS (
                            SELECT COUNT(*) AS fails
                            FROM exam e
                            WHERE e.attemptcount = :attemptCount
                            AND e.mentor_id = :mentorId
                            AND e.success = false
                        )
                        SELECT passCount.passes, failCount.fails,
                            CASE
                                WHEN failCount.fails + passCount.passes = 0 THEN -1
                                ELSE (passCount.passes::float / (failCount.fails + passCount.passes))
                            END AS pass_ratio
                        FROM passCount, failCount;
                    """, nativeQuery = true)
    Map findPassRatio(long mentorId, int attemptCount);

    @Query("SELECT MAX(e.attemptCount) FROM Exam e")
    Integer findHighestAttemptCount();
}
