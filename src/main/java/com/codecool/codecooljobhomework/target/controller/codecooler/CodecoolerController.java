package com.codecool.codecooljobhomework.target.controller.codecooler;

import com.codecool.codecooljobhomework.target.controller.codecooler.dto.NewCodecoolerDto;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.service.codecooler.CodecoolerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/target/codecooler")
public class CodecoolerController {
    private final CodecoolerService codecoolerService;

    @Autowired
    public CodecoolerController(CodecoolerService codecoolerService) {
        this.codecoolerService = codecoolerService;
    }

    @PostMapping
    public ResponseEntity<String> createCodecooler(@RequestBody NewCodecoolerDto newCodecoolerDto) {
        codecoolerService.createCodecooler(newCodecoolerDto);
        return ResponseEntity.ok("New codecooler saved successfully");
    }

    @GetMapping("/averages/{studentId}")
    public ResponseEntity<List<Result>> getAverages(@PathVariable long studentId) {
        return ResponseEntity.ok().body(codecoolerService.getAverages(studentId));
    }

    @GetMapping("/mentorstats/{mentorId}")
    public ResponseEntity<Map> getMentorStats(@PathVariable long mentorId) {
        return ResponseEntity.ok(codecoolerService.getMentorStats(mentorId));
    }
}
