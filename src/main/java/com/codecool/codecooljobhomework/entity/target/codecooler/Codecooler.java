package com.codecool.codecooljobhomework.entity.target.codecooler;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Codecooler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String email;

    private String username;
    private LocalDate birthday;
    private Position position;

    public Codecooler() {
    }
}
