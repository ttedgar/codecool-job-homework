package com.codecool.codecooljobhomework.target.service.exam;

import com.codecool.codecooljobhomework.source.entity.Source;
import com.codecool.codecooljobhomework.source.repository.SourceRepository;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import com.codecool.codecooljobhomework.target.entity.codecooler.Position;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ExamServiceTest {
    @Mock
    private ExamRepository examRepository;

    @Mock
    private CodeCoolerRepository codeCoolerRepository;

    @Mock
    private SourceRepository sourceRepository;

    @Mock
    private ObjectMapper objectMapper;

    private ExamService examService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        examService = new ExamService(examRepository, codeCoolerRepository, sourceRepository, objectMapper);
    }

    @Test
    void testSynchronizeWithNoNewSource() {
        //Arrange
        when(sourceRepository.findAllIds()).thenReturn(List.of(1L, 2L, 3L));
        when(examRepository.findAllSourceIds()).thenReturn(List.of(1L, 2L, 3L));
        DataTransferReport expectedReport = new DataTransferReport();
        //Act
        DataTransferReport actualReport = examService.synchronize();
        //Assert
        assertEquals(expectedReport, actualReport);
    }


    @Test
    void testSynchronizeWithOneNewSource() throws JsonProcessingException {
        //Arrange
        when(sourceRepository.findAllIds()).thenReturn(List.of(1L, 2L, 3L));
        when(examRepository.findAllSourceIds()).thenReturn(List.of(1L, 2L));
        Source source = new Source();
        source.setId(3L);
        source.setContent("{\n" +
                "  \"module\": \"ProgBasics\",\n" +
                "  \"mentor\": \"peter.szarka@codecool.com\",\n" +
                "  \"student\": \"foo@bar.com\",\n" +
                "  \"date\": \"2024-02-01\",\n" +
                "  \"cancelled\": true,\n" +
                "  \"comment\": \"Foo was sick.\"\n" +
                "}");
        when(sourceRepository.findAllByIdIn(List.of(3L))).thenReturn(List.of(source));
        Map<String, Object> examMap = new HashMap<>(Map.of(
                "module", "ProgBasics",
                "mentor", "peter.szarka@codecool.com",
                "student", "foo@bar.com",
                "date", "2024-02-01",
                "cancelled", true,
                "comment", "Foo was sick."
        ));
        when(objectMapper.readValue(source.getContent(), Map.class)).thenReturn(examMap);
        when(codeCoolerRepository.findByEmailAndPosition("peter.szarka@codecool.com", Position.MENTOR))
                .thenReturn(Optional.of(new Codecooler()));
        when(codeCoolerRepository.findByEmailAndPosition("foo@bar.com", Position.STUDENT))
                .thenReturn(Optional.of(new Codecooler()));
        DataTransferReport expectedReport = new DataTransferReport();
        expectedReport.incrementSuccessfulTransfers();
        //Act
        DataTransferReport actualReport = examService.synchronize();
        //Assert
        assertEquals(expectedReport, actualReport);
    }

    @Test
    void testSynchronizeWithOneNewSourceWithInvalidMentorEmail() throws JsonProcessingException {
        //Arrange
        when(sourceRepository.findAllIds()).thenReturn(List.of(1L, 2L, 3L));
        when(examRepository.findAllSourceIds()).thenReturn(List.of(1L, 2L));
        Source source = new Source();
        source.setId(3L);
        source.setContent("{\n" +
                "  \"module\": \"ProgBasics\",\n" +
                "  \"mentor\": \"peter.szarka@codecool.com\",\n" +
                "  \"student\": \"foo@bar.com\",\n" +
                "  \"date\": \"2024-02-01\",\n" +
                "  \"cancelled\": true,\n" +
                "  \"comment\": \"Foo was sick.\"\n" +
                "}");
        when(sourceRepository.findAllByIdIn(List.of(3L))).thenReturn(List.of(source));
        Map<String, Object> examMap = new HashMap<>(Map.of(
                "module", "ProgBasics",
                "mentor", "peter.szarka@codecool.com",
                "student", "foo@bar.com",
                "date", "2024-02-01",
                "cancelled", true,
                "comment", "Foo was sick."
        ));
        when(objectMapper.readValue(source.getContent(), Map.class)).thenReturn(examMap);
        when(codeCoolerRepository.findByEmailAndPosition("peter.szarka@codecool.com", Position.MENTOR))
                .thenReturn(Optional.empty());
        when(codeCoolerRepository.findByEmailAndPosition("foo@bar.com", Position.STUDENT))
                .thenReturn(Optional.of(new Codecooler()));
        DataTransferReport expectedReport = new DataTransferReport();
        expectedReport.incrementFailedTransfers();
        expectedReport.addExceptionMessage("peter.szarka@codecool.com is not a valid mentor email. SourceId: 3");
        //Act
        DataTransferReport actualReport = examService.synchronize();
        //Assert
        assertEquals(expectedReport, actualReport);
    }

}