package com.codecool.codecooljobhomework.target.repository;

import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.entity.exam.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
                with passCount as (
                	select count(*) as passes
                	from exam e
                	join codecooler c
                	on e.mentor_id = c.id
                	join codecooler c2
                	on e.student_id = c2.id
                	where e."date" = (
                		select min(e2.date)
                		from exam e2
                		where e."module" = e2."module"
                		and e.student_id = e2.student_id
                	)
                	and e.mentor_id = :mentorId
                	and e.success
                ),
                failCount as (
                	select count(*) as fails
                	from exam e
                	join codecooler c
                	on e.mentor_id = c.id
                	join codecooler c2
                	on e.student_id = c2.id
                	where e."date" = (
                		select min(e2.date)
                		from exam e2
                		where e."module" = e2."module"
                		and e.student_id = e2.student_id
                	)
                	and e.mentor_id = :mentorId
                	and e.success = false
                )
                select\s
                    case\s
                        when failCount.fails + passCount.passes = 0 then null
                        else (passCount.passes::float / (failCount.fails + passCount.passes))
                    end
                from passCount, failCount;
            """, nativeQuery = true)
    Optional <Double> getPassRatioOnFirstExams(long mentorId);

    @Query(value = """
                with passCount as (
                	select count(*) as passes
                	from exam e
                	join codecooler c
                	on e.mentor_id = c.id
                	join codecooler c2
                	on e.student_id = c2.id
                	where e."date" = (
                		select max(e2.date)
                		from exam e2
                		where e."module" = e2."module"
                		and e.student_id = e2.student_id
                	)
                	and e.mentor_id = :mentorId
                	and e.success
                ),
                failCount as (
                	select count(*) as fails
                	from exam e
                	join codecooler c
                	on e.mentor_id = c.id
                	join codecooler c2
                	on e.student_id = c2.id
                	where e."date" = (
                		select max(e2.date)
                		from exam e2
                		where e."module" = e2."module"
                		and e.student_id = e2.student_id
                	)
                	and e.mentor_id = :mentorId
                	and e.success = false
                )
                select\s
                    case\s
                        when failCount.fails + passCount.passes = 0 then null
                        else (passCount.passes::float / (failCount.fails + passCount.passes))
                    end
                from passCount, failCount;
            """, nativeQuery = true)
    Optional <Double> getPassRatioOnLastExams(long mentorId);
}
