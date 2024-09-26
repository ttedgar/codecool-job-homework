package com.codecool.codecooljobhomework.entity.target.exam;

import com.codecool.codecooljobhomework.entity.target.exam.results.Result;
import com.codecool.codecooljobhomework.entity.target.codecooler.Codecooler;
import com.codecool.codecooljobhomework.entity.target.exam.results.Dimension;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@Entity
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Codecooler mentor;
    @OneToOne
    private Codecooler mentee;

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
    private HashMap<Dimension, Result> results;

    public Exam() {
    }
}
