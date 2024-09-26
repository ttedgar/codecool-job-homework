package com.codecool.codecooljobhomework.target.entity.codecooler;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
public class Codecooler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String email;

    private String username;
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Position position;

    public Codecooler() {
    }

    public Codecooler(String email, String username, LocalDate birthday, Position position) {
        this.email = email;
        this.username = username;
        this.birthday = birthday;
        this.position = position;
    }
}
