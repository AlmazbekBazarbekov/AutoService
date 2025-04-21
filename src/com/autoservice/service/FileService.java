package com.autoservice.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("File read error " + e.getMessage());
        }
        return lines;
    }

    public static void writeFile(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("File input error: " + e.getMessage());
        }
    }

    public static void appendToFile(String filePath, String newLine) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(newLine);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("File append error " + e.getMessage());
        }
    }
}
