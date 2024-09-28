package com.codecool.codecooljobhomework.target.service.codecooler;

import com.codecool.codecooljobhomework.target.controller.codecooler.NewCodecoolerDto;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodecoolerService {
    private final CodeCoolerRepository codeCoolerRepository;

    @Autowired
    public CodecoolerService(CodeCoolerRepository codeCoolerRepository) {
        this.codeCoolerRepository = codeCoolerRepository;
    }

    public void createCodecooler(NewCodecoolerDto newCodecoolerDto) {
        codeCoolerRepository.save(mapNewCodecoolerDtoToCodecooler(newCodecoolerDto));
    }

    private Codecooler mapNewCodecoolerDtoToCodecooler(NewCodecoolerDto newCodecoolerDto) {
        return new Codecooler(
                newCodecoolerDto.email(),
                newCodecoolerDto.username(),
                newCodecoolerDto.birthday(),
                newCodecoolerDto.position()
        );
    }
}
