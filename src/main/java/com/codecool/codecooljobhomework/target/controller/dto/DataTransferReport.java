package com.codecool.codecooljobhomework.target.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class DataTransferReport {
    private int numberOfSuccessfulTransfers = 0;
    private List<ExceptionReport> exceptionReports = new ArrayList<>();

    public DataTransferReport() {
    }

    public void incrementSuccessfulTransfers() {
        numberOfSuccessfulTransfers++;
    }

    public void addExceptionReport(ExceptionReport exceptionReport) {
        exceptionReports.add(exceptionReport);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataTransferReport that)) return false;
        return numberOfSuccessfulTransfers == that.numberOfSuccessfulTransfers && Objects.equals(exceptionReports, that.exceptionReports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfSuccessfulTransfers, exceptionReports);
    }
}
