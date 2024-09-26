package com.codecool.codecooljobhomework.source.controller;

import com.codecool.codecooljobhomework.source.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/source")
public class SourceController {
    private final SourceService sourceService;

    @Autowired
    public SourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @PostMapping
    public ResponseEntity<String> createRawResult(@RequestBody String resultJson) {
        sourceService.createRawResult(resultJson);
        return ResponseEntity.ok("Json saved successfully");
    }
}
