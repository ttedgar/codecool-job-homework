package com.codecool.codecooljobhomework.target.service.exam;

import com.codecool.codecooljobhomework.source.entity.Source;
import com.codecool.codecooljobhomework.source.repository.SourceRepository;
import com.codecool.codecooljobhomework.target.controller.exam.NewExamDto;
import com.codecool.codecooljobhomework.target.controlleradvice.exception.*;
import com.codecool.codecooljobhomework.target.entity.codecooler.Position;
import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.entity.exam.results.DimensionEnum;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        exam.setStudent(codeCoolerRepository
                .findByEmailAndPosition(newExamDto.studentEmail(), Position.STUDENT)
                .orElseThrow(() -> new CodecoolerNotFoundException(newExamDto.studentEmail() + " is not a valid student email")));
        exam.setMentor(codeCoolerRepository
                .findByEmailAndPosition(newExamDto.mentorEmail(), Position.MENTOR)
                .orElseThrow(() -> new CodecoolerNotFoundException(newExamDto.mentorEmail() + " is not a valid mentor email")));
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

    public int synchronize() {
        if (getMissingSourceIds().isEmpty()) return 0;
        List<Source> rawData = sourceRepository.findAllByIdIn(getMissingSourceIds());
        for (Source source : rawData) {
            convertJsonToExam(source);
        }
        return rawData.size();
    }

    private void convertJsonToExam(Source source) {
        Map<String, Object> examMap = parseJsonToMap(source.getContent());
        Exam exam = new Exam();
        exam.setSourceId(source.getId());
        boolean cancelled = mapCancelled(examMap, exam, source.getId());
        mapEmails(examMap, exam, source.getId());
        mapModules(examMap, exam, source.getId());
        mapDate(examMap, exam, source.getId());
        mapSuccess(examMap, exam, source.getId(), cancelled);
        mapComment(examMap, exam, source.getId(), cancelled);
        mapResults(examMap, exam, source.getId(), cancelled);
        examRepository.save(exam);
    }

    private Map<String, Object> parseJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new UnableToParseJsonToMapException("Parsing json to Map<String, Object> failed.");
        }
    }

    private boolean mapCancelled(Map<String, Object> examMap, Exam exam, long sourceId) {
        Boolean cancelled = (Boolean) examMap.get("cancelled");
        if (cancelled != null) exam.setCancelled(cancelled);
        else throw new MissingFieldException("Field \"cancelled\" is required to parse json. SourceId: " + sourceId);
        return cancelled;
    }

    private void mapEmails(Map<String, Object> examMap, Exam exam, long sourceId) {
        String mentorEmail = (String) examMap.get("mentor");
        if (mentorEmail != null) {
            exam.setMentor(codeCoolerRepository
                    .findByEmailAndPosition(mentorEmail, Position.MENTOR)
                    .orElseThrow(() -> new CodecoolerNotFoundException(mentorEmail + " is not a valid student email. SourceId: " + sourceId)));
        } else {
            throw new MissingFieldException("Field \"mentor\" email is required to parse json. SourceId: " + sourceId);
        }

        String studentEmail = (String) examMap.get("student");
        if (studentEmail != null) {
            exam.setStudent(codeCoolerRepository
                    .findByEmailAndPosition(studentEmail, Position.STUDENT)
                    .orElseThrow(() -> new CodecoolerNotFoundException(studentEmail + " is not a valid student email. SourceId: " + sourceId)));
        } else {
            throw new MissingFieldException("Field \"student\" is required to parse json. SourceId: " + sourceId);
        }
    }

    private void mapModules(Map<String, Object> examMap, Exam exam, long sourceId) {
        String module = (String) examMap.get("module");
        if (module != null) exam.setModule(Module.valueOf(module.toUpperCase()));
        else throw new MissingFieldException("Field \"module\" is required to parse json. SourceId: " + sourceId);
    }

    private void mapDate(Map<String, Object> examMap, Exam exam, long sourceId) {
        String date = (String) examMap.get("date");
        if (date != null) {
            try {
                exam.setDate(LocalDate.parse(date));
            } catch (DateTimeParseException e) {
                throw new InvalidDateFormatException("Invalid date format for date: " + date + ". SourceId: " + sourceId);
            }
        }
        else {
            throw new MissingFieldException("Field \"date\" is required to parse json. SourceId: " + sourceId);
        }
    }

    private void mapSuccess(Map<String, Object> examMap, Exam exam, long sourceId, boolean cancelled) {
        Boolean success = (Boolean) examMap.get("success");
        if (success != null) exam.setSuccess(success);
        else if (!cancelled) throw new MissingFieldException("Field \"success\" is required to parse json. SourceId: " + sourceId);
    }

    private void mapComment(Map<String, Object> examMap, Exam exam, long sourceId, boolean cancelled) {
        String comment = (String) examMap.get("comment");
        if (comment != null) exam.setComment(comment);
        else if (!cancelled) throw new MissingFieldException("Field \"comment\" is required to parse json. SourceId: " + sourceId);
    }

    private void mapResults(Map<String, Object> examMap, Exam exam, long sourceId, boolean cancelled) {
        List<Map<String, Object>> resultsList = (List<Map<String, Object>>) examMap.get("results");
        if (resultsList != null) {
            for (Map<String, Object> result : resultsList) {
                Result resultObject = new Result();
                try {
                    resultObject.setDimension(DimensionEnum.valueOf(result.get("dimension").toString().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new InvalidDimensionException(result.get("dimension").toString() + " is an invalid dimension. SourceId: " + sourceId);
                }
                if (((Integer) result.get("result")) >= 0 && ((Integer) result.get("result")) <= 120) {
                    resultObject.setResult((Integer) result.get("result"));
                } else {
                    throw new InvalidResultException("Result should be a number between 0 and 120. SourceId: " + sourceId);
                }
                exam.addResult(resultObject);
            }
        } else if (!cancelled) {
            throw new MissingFieldException("Field \"comment\" is required to parse json. SourceId: " + sourceId);
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
        codeCoolerRepository.findByIdAndPosition(studentId, Position.STUDENT)
                .orElseThrow(() -> new CodecoolerNotFoundException("There is no student with this Id"));

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
