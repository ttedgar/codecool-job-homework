package com.codecool.codecooljobhomework.target.entity.exam;

import com.codecool.codecooljobhomework.target.entity.exam.results.Result;
import com.codecool.codecooljobhomework.target.entity.codecooler.Codecooler;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Codecooler mentor;
    @ManyToOne
    private Codecooler student;

    @Enumerated(EnumType.STRING)
    private Module module;

    @ElementCollection
    private List<Result> results;

    private LocalDateTime date;
    private boolean cancelled;
    private boolean success;
    private boolean isLastAttemptInModule;
    private String comment;

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
                ArrayList<Result> results) {
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
