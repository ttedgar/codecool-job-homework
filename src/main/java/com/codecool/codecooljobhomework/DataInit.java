package com.codecool.codecooljobhomework;

import com.codecool.codecooljobhomework.source.entity.Source;
import com.codecool.codecooljobhomework.source.repository.SourceRepository;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import com.codecool.codecooljobhomework.target.entity.codecooler.Position;
import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.entity.exam.results.DimensionEnum;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInit {
    private final SourceRepository sourceRepository;
    private final CodeCoolerRepository codeCoolerRepository;
    private final ExamRepository examRepository;

    @Autowired
    public DataInit(SourceRepository sourceRepository,
                    CodeCoolerRepository codeCoolerRepository,
                    ExamRepository examRepository) {
        this.sourceRepository = sourceRepository;
        this.codeCoolerRepository = codeCoolerRepository;
        this.examRepository = examRepository;
    }

    @PostConstruct
    public void init() {
        initializeSource();
        initializeCodecoolers();
        initializeExam();
        initializeExam();
    }

    private void initializeExam() {
        Exam exam = new Exam();
        exam.setStudent(codeCoolerRepository.findByEmail("student@example.com"));
        exam.setMentor(codeCoolerRepository.findByEmail("mentor@example.com"));
        exam.setCancelled(false);
        exam.setDate(LocalDateTime.now());
        exam.setModule(Module.OOP);
        exam.setComment("Not bad");
        exam.setResults(List.of(new Result(DimensionEnum.CLEAN_CODE, 80),
                new Result(DimensionEnum.COMMUNICATION, 70),
                new Result(DimensionEnum.CODE_NAVIGATION, 100),
                new Result(DimensionEnum.GEEKNESS, 120)
        ));
        exam.setSuccess(false);
        exam.setLatestAttemptInModule(true);
        examRepository.save(exam);
    }

    private void initializeCodecoolers() {
        Codecooler mentor = new Codecooler();
        mentor.setBirthday(LocalDate.now());
        mentor.setUsername("mentor");
        mentor.setEmail("mentor@example.com");
        mentor.setPosition(Position.MENTOR);

        Codecooler student = new Codecooler();
        student.setBirthday(LocalDate.now());
        student.setUsername("student");
        student.setEmail("student@example.com");
        student.setPosition(Position.STUDENT);

        codeCoolerRepository.saveAll(List.of(mentor, student));
    }

    private void initializeSource() {
        String json1 = "{\n" +
                "  \"module\": \"ProgBasics\",\n" +
                "  \"mentor\": \"peter.szarka@codecool.com\",\n" +
                "  \"student\": \"foo@bar.com\",\n" +
                "  \"date\": \"2024-02-01\",\n" +
                "  \"cancelled\": true,\n" +
                "  \"comment\": \"Foo was sick.\"\n" +
                "}";
        String json2 = "{\n" +
                "  \"module\": \"ProgBasics\",\n" +
                "  \"mentor\": \"peter.szarka@codecool.com\",\n" +
                "  \"student\": \"foo@bar.com\",\n" +
                "  \"date\": \"2024-02-05\",\n" +
                "  \"cancelled\": false,\n" +
                "  \"success\": true,\n" +
                "  \"comment\": \"Everything was ok.\",\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"dimension\": \"Coding\",\n" +
                "      \"result\": 80\n" +
                "    },\n" +
                "    {\n" +
                "      \"dimension\": \"Communication\",\n" +
                "      \"result\": 100\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String json3 = "{\n" +
                "  \"module\": \"Web\",\n" +
                "  \"mentor\": \"mano.fabian@codecool.com\",\n" +
                "  \"student\": \"foo@bar.com\",\n" +
                "  \"date\": \"2024-05-11\",\n" +
                "  \"cancelled\": false,\n" +
                "  \"success\": false,\n" +
                "  \"comment\": \"Couldn't really start, just wrote some HTML.\",\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"dimension\": \"Coding\",\n" +
                "      \"result\": 0\n" +
                "    },\n" +
                "    {\n" +
                "      \"dimension\": \"HTML\",\n" +
                "      \"result\": 30\n" +
                "    },\n" +
                "    {\n" +
                "      \"dimension\": \"Communication\",\n" +
                "      \"result\": 30\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String json4 = "{\"module\": \"Web\",\n" +
                "  \"mentor\": \"peter.szarka@codecool.com\",\n" +
                "  \"student\": \"foo@bar.com\",\n" +
                "  \"date\": \"2024-05-21\",\n" +
                "  \"cancelled\": false,\n" +
                "  \"success\": false,\n" +
                "  \"comment\": \"Wrote spaghetti code, and tried to sell it. Nice page, though.\",\n" +
                "  \"results\": [\n" +
                "    {\"dimension\": \"Coding\",\n" +
                "      \"result\": 20},\n" +
                "    {\"dimension\": \"HTML\",\n" +
                "      \"result\": 100},\n" +
                "    {\"dimension\": \"Communication\",\n" +
                "      \"result\": 80}\n" +
                "  ]}\n";
        sourceRepository.save(new Source(json1));
        sourceRepository.save(new Source(json2));
        sourceRepository.save(new Source(json3));
        sourceRepository.save(new Source(json4));
    }
}
