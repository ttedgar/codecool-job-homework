package com.codecool.codecooljobhomework.target.service.exam;

import com.codecool.codecooljobhomework.target.controller.exam.NewExamDto;
import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final CodeCoolerRepository codeCoolerRepository;

    @Autowired
    public ExamService(ExamRepository examRepository, CodeCoolerRepository codeCoolerRepository) {
        this.examRepository = examRepository;
        this.codeCoolerRepository = codeCoolerRepository;
    }

    @Transactional
    public void createExam(NewExamDto newExamDto) {
        Exam exam = mapNewExamDtoToExam(newExamDto);
        updateLatestAttemptInModule(newExamDto.module());
        examRepository.save(exam);
    }

    private Exam mapNewExamDtoToExam(NewExamDto newExamDto) {
        Exam exam = new Exam();
        exam.setStudent(codeCoolerRepository.findByEmail(newExamDto.studentEmail()));
        exam.setMentor(codeCoolerRepository.findByEmail(newExamDto.mentorEmail()));
        exam.setCancelled(newExamDto.cancelled());
        exam.setDate(newExamDto.date());
        exam.setModule(newExamDto.module());
        exam.setComment(newExamDto.comment());
        exam.setResults(newExamDto.results());
        exam.setSuccess(newExamDto.success());
        return exam;
    }

    private void updateLatestAttemptInModule(Module module) {
        List<Exam> exams = examRepository.findByModule(module);
        exams.forEach(exam -> exam.setLatestAttemptInModule(false));
    }

    public List<Exam> getExams() {
        return examRepository.findAll();
    }
}
