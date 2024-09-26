package com.codecool.codecooljobhomework.target.entity.exam.results;

public enum Result {
    OUTSTANDING(120),
    PERFECT(100),
    GOOD(80),
    AVERAGE(60),
    POOR(40),
    AWFUL(20),
    UNACCEPTABLE(10);

    private int percentage;

    Result(int percentage) {
        this.percentage = percentage;
    }
}
