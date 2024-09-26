package com.codecool.codecooljobhomework.source.service;

import com.codecool.codecooljobhomework.source.entity.Source;
import com.codecool.codecooljobhomework.source.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService {
    private final SourceRepository sourceRepository;

    @Autowired
    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public void createRawResult(String resultJson) {
        sourceRepository.save(new Source(resultJson));
    }
}
