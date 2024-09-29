package com.codecool.codecooljobhomework.target.service.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class DataTransferReport {
    private int numberOfSuccessfulTransfers = 0;
    private int numberOfFailedTransfers = 0;
    private List<String> exceptionMessages = new ArrayList<>();

    public DataTransferReport() {
    }

    public void incrementSuccessfulTransfers() {
        numberOfSuccessfulTransfers++;
    }

    public void incrementFailedTransfers() {
        numberOfFailedTransfers++;
    }

    public void addExceptionMessage(String message) {
        exceptionMessages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataTransferReport that)) return false;
        return numberOfSuccessfulTransfers == that.numberOfSuccessfulTransfers && numberOfFailedTransfers == that.numberOfFailedTransfers && Objects.equals(exceptionMessages, that.exceptionMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfSuccessfulTransfers, numberOfFailedTransfers, exceptionMessages);
    }
}
