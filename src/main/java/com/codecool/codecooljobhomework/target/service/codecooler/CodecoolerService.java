package com.codecool.codecooljobhomework.target.service.codecooler;

import com.codecool.codecooljobhomework.target.controller.codecooler.NewCodecoolerDto;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodecoolerService {

    private final CodeCoolerRepository codeCoolerRepository;
    private final CodecoolerMapper codecoolerMapper;

    @Autowired
    public CodecoolerService(CodeCoolerRepository codeCoolerRepository, CodecoolerMapper codecoolerMapper) {
        this.codeCoolerRepository = codeCoolerRepository;
        this.codecoolerMapper = codecoolerMapper;
    }

    public void createCodecooler(NewCodecoolerDto newCodecoolerDto) {
        codeCoolerRepository.save(codecoolerMapper.mapNewCodecoolerDtoToCodecooler(newCodecoolerDto));
    }
}
