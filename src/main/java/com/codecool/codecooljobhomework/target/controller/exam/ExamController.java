package com.codecool.codecooljobhomework.target.controller.exam;

import com.codecool.codecooljobhomework.target.service.codecooler.CodecoolerService;
import com.codecool.codecooljobhomework.target.service.exam.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/target/exam")
public class ExamController {
    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<String> createExam(@RequestBody NewExamDto newExamDto) {
        examService.createExam(newExamDto);
        return ResponseEntity.ok("New exam saved successfully");
    }

}
