package com.codecool.codecooljobhomework.target.controller.codecooler;

import com.codecool.codecooljobhomework.target.service.codecooler.CodecoolerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
