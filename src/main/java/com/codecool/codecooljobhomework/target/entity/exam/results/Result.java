package com.codecool.codecooljobhomework.target.entity.exam.results;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Result {
    @Enumerated(EnumType.STRING)
    private DimensionEnum dimension;

    @Enumerated(EnumType.STRING)
    private ResultEnum result;

    public Result(DimensionEnum dimension, ResultEnum result) {
        this.dimension = dimension;
        this.result = result;
    }

    public Result() {
    }
}
