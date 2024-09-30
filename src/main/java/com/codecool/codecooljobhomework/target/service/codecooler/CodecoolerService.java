package com.codecool.codecooljobhomework.target.service.codecooler;

import com.codecool.codecooljobhomework.target.controller.codecooler.dto.NewCodecoolerDto;
import com.codecool.codecooljobhomework.target.controller.codecooler.dto.MentorStatisticsReport;
import com.codecool.codecooljobhomework.target.exception.CodecoolerNotFoundException;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import com.codecool.codecooljobhomework.target.entity.codecooler.Position;
import com.codecool.codecooljobhomework.target.entity.exam.results.DimensionEnum;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CodecoolerService {
    private final CodeCoolerRepository codeCoolerRepository;
    private final ExamRepository examRepository;

    private static final Logger logger = LoggerFactory.getLogger(CodecoolerService.class);

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
                .orElseThrow(() -> {
                    String errorMessage = studentId + " is not a valid studentId";
                    logger.error(errorMessage);
                    return new CodecoolerNotFoundException(errorMessage);
                });

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
        Codecooler mentor = codeCoolerRepository.findByIdAndPosition(mentorId, Position.MENTOR)
                .orElseThrow(() -> {
                    String errorMessage = mentorId + " is not a valid mentorId";
                    logger.error(errorMessage);
                    return new CodecoolerNotFoundException(errorMessage);
                });
        Map mentorStats = new LinkedHashMap();
        mentorStats.put("Mentor: ", mentor);
        for (int i = 1; i < examRepository.findHighestAttemptCount(); i++) {
            Map statsMap = examRepository.findPassRatio(mentorId, i);
            MentorStatisticsReport mentorStatisticsReport = new MentorStatisticsReport(
                    (Long) statsMap.get("passes"),
                    (Long) statsMap.get("fails"),
                    (Double) statsMap.get("pass_ratio"));
            mentorStats.put("Attempt " + i, mentorStatisticsReport);
        }
        logger.info(mentorStats + " sent to frontend");
        return mentorStats;
    }
}
