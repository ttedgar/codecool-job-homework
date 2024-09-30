package com.codecool.codecooljobhomework;

import com.codecool.codecooljobhomework.source.entity.Source;
import com.codecool.codecooljobhomework.source.repository.SourceRepository;
import com.codecool.codecooljobhomework.target.exception.InvalidEmailException;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import com.codecool.codecooljobhomework.target.entity.codecooler.Position;
import com.codecool.codecooljobhomework.target.entity.exam.Exam;
import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.entity.exam.results.DimensionEnum;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.repository.CodeCoolerRepository;
import com.codecool.codecooljobhomework.target.repository.ExamRepository;
import com.codecool.codecooljobhomework.util.Util;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    }

    private void initializeExam() {
        Exam exam = new Exam();
        exam.setStudent(codeCoolerRepository
                .findByEmailAndPosition("foo@bar.com", Position.STUDENT)
                .orElseThrow(() -> new InvalidEmailException("foo@bar.com is not a valid student email")));
        exam.setMentor(codeCoolerRepository
                .findByEmailAndPosition("peter.szarka@codecool.com", Position.MENTOR)
                .orElseThrow(() -> new InvalidEmailException("foo@bar.com is not a valid student email")));
        exam.setCancelled(false);
        exam.setDate(LocalDate.now());
        exam.setModule(Module.OOP);
        exam.setComment("Not bad");
        exam.setResults(List.of(new Result(DimensionEnum.CLEAN_CODE, 80),
                new Result(DimensionEnum.COMMUNICATION, 70),
                new Result(DimensionEnum.CODING, 100),
                new Result(DimensionEnum.GEEKNESS, 120)
        ));
        exam.setSuccess(false);
        exam.setSourceId(200);
        exam.setAttemptCount(1);
        examRepository.save(exam);
    }

    private void initializeCodecoolers() {
        List<Codecooler> codecCoolers = List.of(
                createCodecooler("mentor", "peter.szarka@codecool.com", Position.MENTOR),
                createCodecooler("mentor", "mano.fabian@codecool.com", Position.MENTOR),
                createCodecooler("mentor", "artur.kovacs@codecool.com", Position.MENTOR),
                createCodecooler("student", "foo@bar.com", Position.STUDENT),
                createCodecooler("student", "foo2@bar.com", Position.STUDENT),
                createCodecooler("student", "foo3@bar.com", Position.STUDENT)
        );
        codeCoolerRepository.saveAll(codecCoolers);
    }

    private Codecooler createCodecooler(String username, String email, Position position) {
        Codecooler codecooler = new Codecooler();
        codecooler.setBirthday(LocalDate.now());
        codecooler.setUsername(username);
        codecooler.setEmail(email);
        codecooler.setPosition(position);
        return codecooler;
    }

    private void initializeSource() {
        sourceRepository.saveAll(Util.readTxtFile("./src/main/resources/sourceData.txt").stream().map(Source::new).toList());
    }
}
