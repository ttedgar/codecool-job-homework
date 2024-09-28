package com.codecool.codecooljobhomework.target.service.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
}
