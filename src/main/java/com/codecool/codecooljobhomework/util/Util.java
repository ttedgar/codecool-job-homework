package com.codecool.codecooljobhomework.util;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<String> readTxtFile(String filePath) {
        List<String> result = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] parts = content.split("###");
            for (String part : parts) {
                result.add(part.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
