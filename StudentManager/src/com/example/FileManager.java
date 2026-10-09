package com.example;

import java.io.*;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "students.csv";

    public void saveToFile(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println("id,surname,name,group,averageGrade");
            for (Student s : students) {
                writer.println(s.getId() + "," + s.getSurname() + "," + s.getName() + "," +
                        s.getGroup() + "," + s.getAverageGrade());
            }
            System.out.println("Данные успешно сохранены в " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    public void loadFromFile(StudentRepository repository) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Файл " + FILE_NAME + " не найден. Начинаем с пустой базы.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String surname = parts[1];
                    String name = parts[2];
                    String group = parts[3];
                    double avg = Double.parseDouble(parts[4]);
                    Student s = new Student(id, surname, name, group, avg);
                    repository.addStudent(s);
                }
            }
            System.out.println("Данные успешно загружены из " + FILE_NAME);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
        }
    }
}
