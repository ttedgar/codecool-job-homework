package com.codecool.codecooljobhomework.target.entity.exam;

import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import com.codecool.codecooljobhomework.target.entity.exam.results.Dimension;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Setter
@Getter
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Codecooler mentor;
    @OneToOne
    private Codecooler student;

    private Module module;
    private LocalDateTime date;
    private boolean cancelled;
    private boolean success;
    private boolean isLastAttemptInModule;
    private String comment;

    @ElementCollection
    @CollectionTable(name = "exam_results", joinColumns = @JoinColumn(name = "exam_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Enumerated(EnumType.STRING)
    private Map<Dimension, Result> results;

    public Exam() {
    }

    public Exam(Codecooler mentor,
                Codecooler student,
                Module module,
                LocalDateTime date,
                boolean cancelled,
                boolean success,
                boolean isLastAttemptInModule,
                String comment,
                HashMap<Dimension, Result> results) {
        this.mentor = mentor;
        this.student = student;
        this.module = module;
        this.date = date;
        this.cancelled = cancelled;
        this.success = success;
        this.isLastAttemptInModule = isLastAttemptInModule;
        this.comment = comment;
        this.results = results;
    }


}
