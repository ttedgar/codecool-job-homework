package com.codecool.codecooljobhomework.target.service.codecooler;

import com.codecool.codecooljobhomework.target.controller.codecooler.NewCodecoolerDto;
import com.codecool.codecooljobhomework.target.controlleradvice.exception.CodecoolerNotFoundException;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import com.codecool.codecooljobhomework.target.entity.codecooler.Position;
import com.codecool.codecooljobhomework.target.entity.exam.results.DimensionEnum;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CodecoolerService {
    private final CodeCoolerRepository codeCoolerRepository;
    private final ExamRepository examRepository;

    @Autowired
    public CodecoolerService(CodeCoolerRepository codeCoolerRepository, ExamRepository examRepository) {
        this.codeCoolerRepository = codeCoolerRepository;
        this.examRepository = examRepository;
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

    public List<Result> getAverages(long studentId) {
        codeCoolerRepository.findByIdAndPosition(studentId, Position.STUDENT)
                .orElseThrow(() -> new CodecoolerNotFoundException("There is no student with this Id. studentId: " + studentId));

        List<Object[]> results = examRepository.findAverageResultsOfLatestExamsByStudentId(studentId);
        List<Result> resultList = new ArrayList<>();
        for (Object[] result : results) {
            DimensionEnum dimension = DimensionEnum.valueOf(result[0].toString().toUpperCase());
            int resultInt = ((BigDecimal) result[1]).intValue();
            resultList.add(new Result(dimension, resultInt));
        }
        return resultList;
    }

    public Map getMentorStats(long mentorId) {
        codeCoolerRepository.findByIdAndPosition(mentorId, Position.MENTOR)
                .orElseThrow(() -> new CodecoolerNotFoundException("There is no mentor with this Id. mentorId: " + mentorId));
        double firstRatio = examRepository.getPassRatioOnFirstExams(mentorId).orElse(-1.0);
        double lastRatio = examRepository.getPassRatioOnLastExams(mentorId).orElse(-1.0);
        return Map.of(
                "Ratio of successful first exams: ", firstRatio,
                "Ratio of successful last exams: ", lastRatio
        );
    }
}
