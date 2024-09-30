package com.codecool.codecooljobhomework.target.service.exam;

import com.codecool.codecooljobhomework.source.entity.Source;
import com.codecool.codecooljobhomework.source.repository.SourceRepository;
import com.codecool.codecooljobhomework.target.controller.exam.NewExamDto;
import com.codecool.codecooljobhomework.target.exceptionhandling.exception.*;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import com.codecool.codecooljobhomework.target.entity.codecooler.Position;
import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.entity.exam.results.DimensionEnum;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import com.codecool.codecooljobhomework.target.controller.dto.DataTransferReport;
import com.codecool.codecooljobhomework.target.controller.dto.ExceptionReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final int MAX_EXAM_PERCENTAGE = 120;
    private static final int MIN_SUCCESSFUL_EXAM_PERCENTAGE = 60;

    private final ExamRepository examRepository;
    private final CodeCoolerRepository codeCoolerRepository;
    private final SourceRepository sourceRepository;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(ExamService.class);

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
        Codecooler student = codeCoolerRepository
                .findByEmailAndPosition(newExamDto.studentEmail(), Position.STUDENT)
                .orElseThrow(() -> {
                    String errorMessage = newExamDto.studentEmail() + " is not a valid student email";
                    logger.error(errorMessage);
                    return new CodecoolerNotFoundException(errorMessage);
                });
        exam.setStudent(student);
        exam.setMentor(codeCoolerRepository
                .findByEmailAndPosition(newExamDto.mentorEmail(), Position.MENTOR)
                .orElseThrow(() -> {
                    String errorMessage = newExamDto.mentorEmail() + " is not a valid mentor email";
                    logger.error(errorMessage);
                    return new CodecoolerNotFoundException(errorMessage);
                }));
        exam.setCancelled(newExamDto.cancelled());
        exam.setDate(newExamDto.date());
        exam.setModule(newExamDto.module());
        exam.setComment(newExamDto.comment());
        exam.setResults(newExamDto.results());
        exam.setSuccess(newExamDto.success());
        exam.setAttemptCount(findAttemptCount(student.getId(), newExamDto.module().toString()));
        return exam;
    }

    public List<Exam> getExams() {
        return examRepository.findAll();
    }

    public DataTransferReport synchronizeSourceAndTargetDbs() {
        DataTransferReport dataTransferReport = new DataTransferReport();
        if (getMissingSourceIds().isEmpty()) {
            dataTransferReport.setNumberOfSuccessfulTransfers(0);
            return dataTransferReport;
        }
        List<Source> rawData = sourceRepository.findAllByIdIn(getMissingSourceIds());
        for (Source source : rawData) {
            try {
                examRepository.save(convertJsonToExam(source));
                dataTransferReport.incrementSuccessfulTransfers();
            } catch (SynchronizationException e) {

                logger.error(e.getMessage());
                dataTransferReport.addExceptionReport(new ExceptionReport(e.getMessage(), e.getClass()));
            }
        }
        return dataTransferReport;
    }

    private Exam convertJsonToExam(Source source) {
        Map<String, Object> examMap = parseJsonToMap(source.getContent(), source.getId());
        Exam exam = new Exam();
        try {
            exam.setSourceId(source.getId());
            boolean cancelled = mapCancelled(examMap, exam, source.getId());
            long studentId = mapStudentEmail(examMap, exam, source.getId());
            mapMentorEmail(examMap, exam, source.getId());
            String moduleStr = mapModules(examMap, exam, source.getId());
            mapDate(examMap, exam, source.getId());
            mapComment(examMap, exam, source.getId());
            if (!cancelled) {
                Boolean success = mapSuccess(examMap, exam, source.getId());
                mapResults(examMap, exam, source.getId(), success);
            }
            exam.setAttemptCount(findAttemptCount(studentId, moduleStr));
        } catch (NullPointerException e) {
            throw new InvalidJsonFieldNameException("Invalid field name. SourceId: " + source.getId());
        }
        return exam;
    }

    private Map<String, Object> parseJsonToMap(String json, long sourceId) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new UnableToParseJsonToMapException("Parsing json to Map<String, Object> failed. SourceId: " + sourceId);
        }
    }

    private boolean mapCancelled(Map<String, Object> examMap, Exam exam, long sourceId) {
        Boolean cancelled = (Boolean) examMap.get("cancelled");
        if (cancelled == null) throw new MissingFieldException("Field \"cancelled\" is required to parse json. SourceId: " + sourceId);
        exam.setCancelled(cancelled);
        return cancelled;
    }

    private long mapStudentEmail(Map<String, Object> examMap, Exam exam, long sourceId) {
        String studentEmail = (String) examMap.get("student");
        if (studentEmail == null) throw new MissingFieldException("Field \"student\" is required to parse json. SourceId: " + sourceId);

        Codecooler student = codeCoolerRepository
                .findByEmailAndPosition(studentEmail, Position.STUDENT)
                .orElseThrow(() -> new InvalidEmailException(studentEmail + " is not a valid student email. SourceId: " + sourceId));
        exam.setStudent(student);
        return student.getId();
    }

    private String mapMentorEmail(Map<String, Object> examMap, Exam exam, long sourceId) {
        String mentorEmail = (String) examMap.get("mentor");
        if (mentorEmail == null) throw new MissingFieldException("Field \"mentor\" email is required to parse json. SourceId: " + sourceId);

        exam.setMentor(codeCoolerRepository
                .findByEmailAndPosition(mentorEmail, Position.MENTOR)
                .orElseThrow(() -> new InvalidEmailException(mentorEmail + " is not a valid mentor email. SourceId: " + sourceId)));
        return mentorEmail;
    }


    private String mapModules(Map<String, Object> examMap, Exam exam, long sourceId) {
        String module = (String) examMap.get("module");
        if (module == null) throw new MissingFieldException("Field \"module\" is required to parse json. SourceId: " + sourceId);
        try {
            exam.setModule(Module.valueOf(module.toUpperCase()));
            return module;
        } catch (IllegalArgumentException e) {
            throw new InvalidModuleException(module + " is not a module. SourceId: " + sourceId);
        }
    }

    private void mapDate(Map<String, Object> examMap, Exam exam, long sourceId) {
        String date = (String) examMap.get("date");
        if (date == null) throw new MissingFieldException("Field \"date\" is required to parse json. SourceId: " + sourceId);
        try {
            exam.setDate(LocalDate.parse(date));
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format for date: " + date + ". SourceId: " + sourceId);
        }
    }

    private boolean mapSuccess(Map<String, Object> examMap, Exam exam, long sourceId) {
        Boolean success = (Boolean) examMap.get("success");
        if (success == null)throw new MissingFieldException("Field \"success\" is required to parse json. SourceId: " + sourceId);
        exam.setSuccess(success);
        return success;
    }

    private void mapComment(Map<String, Object> examMap, Exam exam, long sourceId) {
        String comment = (String) examMap.get("comment");
        if (comment == null) throw new MissingFieldException("Field \"comment\" is required to parse json. SourceId: " + sourceId);
        exam.setComment(comment);
    }

    private int mapResult(Result resultObject, Map<String, Object> result, Integer lowestResult, long sourceId) {
        try {
            resultObject.setDimension(DimensionEnum.valueOf(result.get("dimension").toString().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidDimensionException(result.get("dimension").toString() + " is an invalid dimension. SourceId: " + sourceId);
        }
        try {
            Integer resultInt = (Integer) result.get("result");
            if (resultInt >= 0 && resultInt <= MAX_EXAM_PERCENTAGE) {
                if (resultInt < lowestResult) lowestResult = resultInt;
                resultObject.setResult((Integer) result.get("result"));
            } else {
                throw new InvalidResultException("Result should be a number between 0 and 120. SourceId: " + sourceId);
            }
        } catch (ClassCastException e) {
            throw new InvalidResultException("Result should be a number between 0 and 120. SourceId: " + sourceId);
        }
        return lowestResult;
    }

    private void mapResults(Map<String, Object> examMap, Exam exam, long sourceId, Boolean success) {
        int lowestResult = MAX_EXAM_PERCENTAGE;
        List<Map<String, Object>> resultsList = (List<Map<String, Object>>) examMap.get("results");

        if (resultsList == null) {
            throw new MissingFieldException("Field \"comment\" is required to parse json. SourceId: " + sourceId);
        }

        for (Map<String, Object> result : resultsList) {
            Result resultObject = new Result();
            lowestResult = mapResult(resultObject, result, lowestResult, sourceId);
            validateSuccessAndResult(success, lowestResult, sourceId);
            exam.addResult(resultObject);
        }
    }

    private void validateSuccessAndResult(Boolean success, int lowestResult, long sourceId) {
        if (success && lowestResult < MIN_SUCCESSFUL_EXAM_PERCENTAGE) {
            throw new AmbivalentResultAndSuccessDataException("Exam with result lower then 60% can not be successful. SourceId: " + sourceId);
        } else if (!success && lowestResult > MIN_SUCCESSFUL_EXAM_PERCENTAGE) {
            throw new AmbivalentResultAndSuccessDataException("Exam with all results above 60% can not be unsuccessful. SourceId: " + sourceId);
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

    public int findAttemptCount(long studentId, String moduleStr) {
        return examRepository.findNumberOfAttempts(studentId, moduleStr.toUpperCase()) + 1;
    }
}
