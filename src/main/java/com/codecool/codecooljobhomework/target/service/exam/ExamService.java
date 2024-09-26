package com.codecool.codecooljobhomework.target.service.exam;

import com.codecool.codecooljobhomework.target.controller.exam.NewExamDto;
import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final CodeCoolerRepository codeCoolerRepository;

    @Autowired
    public ExamService(ExamRepository examRepository, CodeCoolerRepository codeCoolerRepository) {
        this.examRepository = examRepository;
        this.codeCoolerRepository = codeCoolerRepository;
    }

    public void createExam(NewExamDto newExamDto) {
        Exam exam = new Exam();
        exam.setStudent(codeCoolerRepository.findByEmail(newExamDto.studentEmail()));
        exam.setMentor(codeCoolerRepository.findByEmail(newExamDto.mentorEmail()));
        exam.setCancelled(newExamDto.cancelled());
        exam.setDate(newExamDto.date());
        exam.setModule(newExamDto.module());
        exam.setComment(newExamDto.comment());
        exam.setResults(newExamDto.results());
        exam.setSuccess(newExamDto.success());
        exam.setLastAttemptInModule(newExamDto.isLastAttemptInModule());
        examRepository.save(exam);
    }

}
