package com.codecool.codecooljobhomework.target.controller.exam;

import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.service.codecooler.CodecoolerService;
import com.codecool.codecooljobhomework.target.service.exam.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/target/exam")
public class ExamController {
    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createExam(@RequestBody NewExamDto newExamDto) {
        examService.createExam(newExamDto);
        return ResponseEntity.ok("New exam saved successfully");
    }

    @GetMapping
    public ResponseEntity<List<Exam>> getExams() {
        List<Exam> exams = examService.getExams();
        return ResponseEntity.ok().body(exams);
    }

    @GetMapping("/sync")
    public ResponseEntity<String> synchronize() {
        boolean isNewData = examService.synchronize();
        return isNewData ?
                ResponseEntity.ok("New data added to target databse") :
                ResponseEntity.ok("Source and target database were already in sync");
    }
}
