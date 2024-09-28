package com.codecool.codecooljobhomework.target.service.exam;

import com.codecool.codecooljobhomework.source.entity.Source;
import com.codecool.codecooljobhomework.source.repository.SourceRepository;
import com.codecool.codecooljobhomework.target.controller.exam.NewExamDto;
import com.codecool.codecooljobhomework.target.controlleradvice.exception.CodecoolerNotFoundException;
import com.codecool.codecooljobhomework.target.controlleradvice.exception.CodecoolerPositionMismatchException;
import com.codecool.codecooljobhomework.target.controlleradvice.exception.UnableToParseJsonToValidExamObject;
import com.codecool.codecooljobhomework.target.entity.codecooler.Position;
import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.entity.exam.results.DimensionEnum;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final CodeCoolerRepository codeCoolerRepository;
    private final SourceRepository sourceRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExamService(ExamRepository examRepository,
                       CodeCoolerRepository codeCoolerRepository,
                       SourceRepository sourceRepository,
                       ObjectMapper objectMapper) {
        this.examRepository = examRepository;
        this.codeCoolerRepository = codeCoolerRepository;
        this.sourceRepository = sourceRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void createExam(NewExamDto newExamDto) {
        Exam exam = mapNewExamDtoToExam(newExamDto);
        examRepository.save(exam);
    }

    private Exam mapNewExamDtoToExam(NewExamDto newExamDto) {
        Exam exam = new Exam();
        exam.setStudent(codeCoolerRepository.findByEmail(newExamDto.studentEmail()));
        exam.setMentor(codeCoolerRepository.findByEmail(newExamDto.mentorEmail()));
        exam.setCancelled(newExamDto.cancelled());
        exam.setDate(newExamDto.date());
        exam.setModule(newExamDto.module());
        exam.setComment(newExamDto.comment());
        exam.setResults(newExamDto.results());
        exam.setSuccess(newExamDto.success());
        return exam;
    }

    public List<Exam> getExams() {
        return examRepository.findAll();
    }

    public boolean synchronize() {
        List<Long> missingSourceIds = getMissingSourceIds();
        if (missingSourceIds.isEmpty()) return false;
        List<Source> rawData = sourceRepository.findAllByIdIn(missingSourceIds);
        for (Source source : rawData) {
            mapJsonToExam(source);
        }
        return true;
    }

    private void mapJsonToExam(Source source) {
        try {
            Map<String, Object> examMap = objectMapper.readValue(source.getContent(), Map.class);
            Exam exam = new Exam();
            exam.setSourceId(source.getId());

            String module = (String) examMap.get("module");
            if (module != null) exam.setModule(Module.valueOf(module.toUpperCase()));

            String mentorEmail = (String) examMap.get("mentor");
            if (mentorEmail != null) exam.setMentor(codeCoolerRepository.findByEmail(mentorEmail));

            String studentEmail = (String) examMap.get("student");
            if (studentEmail != null) exam.setStudent(codeCoolerRepository.findByEmail(studentEmail));

            String date = (String) examMap.get("date");
            if (date != null) exam.setDate(LocalDate.parse(date));

            Boolean cancelled = (Boolean) examMap.get("cancelled");
            if (cancelled != null) exam.setCancelled(cancelled);

            Boolean success = (Boolean) examMap.get("success");
            if (success != null) exam.setSuccess(success);

            String comment = (String) examMap.get("comment");
            if (comment != null) exam.setComment(comment);

            mapResultsToExam(examMap, exam);
            examRepository.save(exam);
        } catch (UnableToParseJsonToValidExamObject e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private void mapResultsToExam(Map<String, Object> examMap, Exam exam) {
        List<Map<String, Object>> resultsList = (List<Map<String, Object>>) examMap.get("results");
        if (resultsList != null) {
            for (Map<String, Object> result : resultsList) {
                Result resultObject = new Result();
                resultObject.setDimension(DimensionEnum.valueOf(result.get("dimension").toString().toUpperCase()));
                resultObject.setResult((Integer) result.get("result"));
                exam.addResult(resultObject);
            }
        }
    }

    private List<Long> getMissingSourceIds() {
        List<Long> sourceIds = sourceRepository.findAllIds();
        List<Long> examIds = examRepository.findAllSourceIds();
        return sourceIds.stream()
                .filter(sourceId -> !examIds.contains(sourceId))
                .toList();
    }

    public List<Result> getAverages(long studentId) {
        codeCoolerRepository.findById(studentId)
                .orElseThrow(() -> new CodecoolerNotFoundException("There is no codecooler with this Id"));
        codeCoolerRepository.findByIdAndPosition(studentId, Position.STUDENT)
                .orElseThrow(() -> new CodecoolerPositionMismatchException("Codecooler with this Id is a mentor, and has no exam results"));

        List<Object[]> results = examRepository.findAverageResultsOfLatestExamsByStudentId(studentId);
        List<Result> resultList = new ArrayList<>();
        for (Object[] result : results) {
            DimensionEnum dimension = DimensionEnum.valueOf(result[0].toString().toUpperCase());
            int resultInt = ((BigDecimal) result[1]).intValue();
            resultList.add(new Result(dimension, resultInt));
        }
        return resultList;
    }
}
