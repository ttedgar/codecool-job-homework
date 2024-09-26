package com.codecool.codecooljobhomework.target.service.codecooler;


import com.codecool.codecooljobhomework.target.controller.codecooler.NewCodecoolerDto;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import org.springframework.stereotype.Component;

@Component
public class CodecoolerMapper {

    public Codecooler mapNewCodecoolerDtoToCodecooler(NewCodecoolerDto newCodecoolerDto) {
        return new Codecooler(
                newCodecoolerDto.email(),
                newCodecoolerDto.username(),
                newCodecoolerDto.birthday(),
                newCodecoolerDto.position()
        );
    }
}
